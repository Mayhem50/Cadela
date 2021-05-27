import { jest } from "@jest/globals"

const InvalidParamsError = (message) => ({
  message,
  name: "MissingParamsError"
})

const makeUserRepository = () => {
  const save = jest.fn((user) => {
    return "any_token"
  })
  return { save }
}

const userRepository = makeUserRepository()

const makeEmailValidator = (isValid = true) => {
  const valid = (email) => {
    return isValid
  }

  return {
    valid
  }
}

const emailValidator = makeEmailValidator()

const makeSignupService = (userRepository, emailValidator) => {
  const signup = (user) => {
    if (!user) {
      throw InvalidParamsError("user")
    }
    if (!user.firstName) {
      throw InvalidParamsError("firstName")
    }
    if (!user.lastName) {
      throw InvalidParamsError("lastName")
    }
    if (!user.email) {
      throw InvalidParamsError("email")
    }
    if (!user.password) {
      throw InvalidParamsError("password")
    }

    if (!emailValidator.valid(user.email)) {
      throw InvalidParamsError("email")
    }
    const token = userRepository.save(user)
    return {
      body: { token }
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
    const ret = signupService.signup(user)

    expect(userRepository.save).toHaveBeenCalledWith(user)
    expect(ret.body).toHaveProperty("token")
  })

  it("Throw an error if user is undefined", () => {
    const signupService = makeSignupService(userRepository, emailValidator)

    expect(signupService.signup).toThrow(InvalidParamsError("user"))
  })

  it("Throw an error if user.firstName is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {}
    expect(() => signupService.signup(user)).toThrow(
      InvalidParamsError("firstName")
    )
  })

  it("Throw an error if user.lastName is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = { firstName: "John" }
    expect(() => signupService.signup(user)).toThrow(
      InvalidParamsError("lastName")
    )
  })

  it("Throw an error if user.email is empty", () => {
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = { firstName: "John", lastName: "McLane" }
    expect(() => signupService.signup(user)).toThrow(
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
    expect(() => signupService.signup(user)).toThrow(
      InvalidParamsError("password")
    )
  })

  it("Throw an error if user.email is not valid", () => {
    const emailValidator = makeEmailValidator(false)
    const signupService = makeSignupService(userRepository, emailValidator)
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "invalid_email",
      password: "any_password"
    }
    expect(() => signupService.signup(user)).toThrow(
      InvalidParamsError("email")
    )
  })
})
