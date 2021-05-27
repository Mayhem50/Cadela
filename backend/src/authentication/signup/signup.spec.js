import { jest } from "@jest/globals"

const InvalidParamsError = (message) => ({
  message,
  name: "MissingParamsError"
})

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
      throw InvalidParamsError("user")
    }
    const { firstName, lastName, email, password } = user
    if (!firstName) {
      throw InvalidParamsError("firstName")
    }
    if (!lastName) {
      throw InvalidParamsError("lastName")
    }
    if (!email) {
      throw InvalidParamsError("email")
    }
    if (!password) {
      throw InvalidParamsError("password")
    }

    if (!emailValidator.valid(email)) {
      throw InvalidParamsError("email")
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

    expect(signupService.signup).rejects.toThrow(InvalidParamsError("user"))
  })

  it("Throw an error if user.firstName is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {}
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamsError("firstName")
    )
  })

  it("Throw an error if user.lastName is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = { firstName: "John" }
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamsError("lastName")
    )
  })

  it("Throw an error if user.email is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = { firstName: "John", lastName: "McLane" }
    expect(() => signupService.signup(user)).rejects.toThrow(
      InvalidParamsError("email")
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
      InvalidParamsError("password")
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
      InvalidParamsError("email")
    )
  })
})
