import { jest } from "@jest/globals"
import { InvalidParamError } from "./invalid-param-error"

const makeUserRepository = () => {
  const save = jest.fn(async (user) => {})
  return { save }
}

const userRepository = makeUserRepository()

const makeEmailValidator = ({ isValid } = { isValid: true }) => {
  const valid = (email) => {
    return isValid
  }

  return {
    valid
  }
}

const emailValidator = makeEmailValidator()

const makeSignupService = (userRepository, emailValidator) => {
  const signup = async (user) => {
    if (!user) {
      throw InvalidParamError("user")
    }
    const { firstName, lastName, email, password } = user
    if (!firstName) {
      throw InvalidParamError("firstName")
    }
    if (!lastName) {
      throw InvalidParamError("lastName")
    }
    if (!email) {
      throw InvalidParamError("email")
    }
    if (!password) {
      throw InvalidParamError("password")
    }

    if (!emailValidator.valid(email)) {
      throw InvalidParamError("email")
    }
    await userRepository.save(user)
    return {
      body: { token: "any_token" }
    }
  }

  return { signup }
}

describe("Signup", () => {
  it("Save user and return token", async () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com",
      password: "any_password"
    }
    const ret = await signupService.signup(user)

    expect(userRepository.save).toHaveBeenCalledWith(user)
    expect(ret.body).toHaveProperty("token")
  })

  it("Throw an error if user is undefined", () => {
    const signupService = makeSignupService(userRepository, emailValidator)

    expect(signupService.signup).rejects.toThrow(InvalidParamError("user"))
  })

  it("Throw an error if user.firstName is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {}
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamError("firstName")
    )
  })

  it("Throw an error if user.lastName is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = { firstName: "John" }
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamError("lastName")
    )
  })

  it("Throw an error if user.email is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = { firstName: "John", lastName: "McLane" }
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamError("email")
    )
  })

  it("Throw an error if user.password is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com"
    }
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamError("password")
    )
  })

  it("Throw an error if user.email is not valid", () => {
    const emailValidator = makeEmailValidator({ isValid: false })
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "invalid_email",
      password: "any_password"
    }
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamError("email")
    )
  })
})
