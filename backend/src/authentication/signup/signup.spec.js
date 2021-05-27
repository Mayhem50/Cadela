import { jest } from "@jest/globals"
import { InvalidParamError } from "./invalid-param-error"

const USER_ID = 1664

const makeUserRepository = () => {
  const save = jest.fn(async (user) => {
    return USER_ID
  })
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

const makeTokenGenerator = () => {
  const generate = jest.fn((userId) => {
    return "any_token"
  })

  return { generate }
}

const tokenGenerator = makeTokenGenerator()

const InternalError = () => ({
  message: "Internal server error",
  name: "InternalError"
})

const makeSignupService = (userRepository, emailValidator, tokenGenerator) => {
  const signup = async (user) => {
    if (!userRepository || !emailValidator || !tokenGenerator) {
      throw InternalError()
    }
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
    const userId = await userRepository.save(user)
    const token = tokenGenerator.generate(userId)
    return {
      body: { token }
    }
  }

  return { signup }
}

describe("Signup", () => {
  it("Save user and return token", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com",
      password: "any_password"
    }
    const ret = await signupService.signup(user)

    expect(userRepository.save).toHaveBeenCalledWith(user)
    expect(tokenGenerator.generate).toHaveBeenCalledWith(USER_ID)
    expect(ret.body).toHaveProperty("token")
  })

  it("Throw an error if any injection is missing", async () => {
    const signupService = makeSignupService()
    await expect(signupService.signup()).rejects.toEqual(InternalError())
  })

  it("Throw an error if user is undefined", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )

    await expect(signupService.signup()).rejects.toEqual(
      InvalidParamError("user")
    )
  })

  it("Throw an error if user.firstName is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )
    const user = {}
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("firstName")
    )
  })

  it("Throw an error if user.lastName is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )
    const user = { firstName: "John" }
    await expect(() => signupService.signup(user)).rejects.toEqual(
      InvalidParamError("lastName")
    )
  })

  it("Throw an error if user.email is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )
    const user = { firstName: "John", lastName: "McLane" }
    await expect(() => signupService.signup(user)).rejects.toEqual(
      InvalidParamError("email")
    )
  })

  it("Throw an error if user.password is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com"
    }
    await expect(() => signupService.signup(user)).rejects.toEqual(
      InvalidParamError("password")
    )
  })

  it("Throw an error if user.email is not valid", async () => {
    const emailValidator = makeEmailValidator({ isValid: false })
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator
    )
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "invalid_email",
      password: "any_password"
    }
    await expect(() => signupService.signup(user)).rejects.toEqual(
      InvalidParamError("email")
    )
  })
})
