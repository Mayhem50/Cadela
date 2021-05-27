import { jest, beforeEach } from "@jest/globals"
import { InternalError } from "./internal-error"
import { InvalidParamError } from "./invalid-param-error"
import {
  UserRepositoryContract,
  COMPLETE_USER
} from "./user-repository.contract"

const USER_ID = 1664

const makeUserRepository = () => {
  let users = []
  let userId

  const obfuscateUser = (user) => {
    delete user.password
    return user
  }

  const save = jest.fn(async (user) => {
    userId = users.push(user).toString()
    return userId
  })

  const getByEmail = jest.fn(async (email) => {
    return users[0] && obfuscateUser(users[0])
  })

  const getUserId = () => userId

  return { save, getByEmail, getUserId }
}

const makeEmailValidator = ({ isValid } = { isValid: true }) => {
  const valid = (email) => {
    return isValid
  }

  return {
    valid
  }
}

const makeTokenGenerator = () => {
  const generate = jest.fn((userId) => {
    return "any_token"
  })

  return { generate }
}

const makeEncrypter = () => {
  const encrypt = jest.fn(async (password) => {
    return password
  })
  return { encrypt }
}

const makeSignupService = (
  userRepository,
  emailValidator,
  tokenGenerator,
  encrypter
) => {
  const signup = async (user) => {
    try {
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
      const userFound = await userRepository.getByEmail(email)

      if (userFound) {
        throw InternalError("User already exists")
      }

      const hashedPassword = await encrypter.encrypt(user.password)

      const userToSave = { ...user, password: hashedPassword }

      const userId = await userRepository.save(userToSave)
      const token = tokenGenerator.generate(userId)
      return {
        body: { token }
      }
    } catch (error) {
      if (error.stack?.includes("TypeError")) {
        throw InternalError()
      }
      throw error ?? InternalError()
    }
  }

  return { signup }
}

describe("Signup", () => {
  let userRepository
  let emailValidator
  let tokenGenerator
  let encrypter

  beforeEach(() => {
    userRepository = makeUserRepository()
    emailValidator = makeEmailValidator()
    tokenGenerator = makeTokenGenerator()
    encrypter = makeEncrypter()
  })

  it("Save user and return token", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )
    const user = COMPLETE_USER
    const ret = await signupService.signup(user)

    expect(encrypter.encrypt).toHaveBeenCalledWith(user.password)
    expect(userRepository.save).toHaveBeenCalledWith(user)
    expect(tokenGenerator.generate).toHaveBeenCalledWith(
      userRepository.getUserId()
    )
    expect(ret.body).toHaveProperty("token")
  })

  it("Throw an error if user already exists", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com",
      password: "any_password"
    }
    await signupService.signup(user)
    await expect(signupService.signup(user)).rejects.toEqual(
      InternalError("User already exists")
    )
  })

  it("Throw an error if any injection is missing", async () => {
    const signupService = makeSignupService()

    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com",
      password: "any_password"
    }

    await expect(signupService.signup(user)).rejects.toEqual(InternalError())
  })

  it("Throw an error if any injection is malformed", async () => {
    userRepository = {}
    tokenGenerator = {}
    emailValidator = {}
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )

    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com",
      password: "any_password"
    }

    await expect(signupService.signup(user)).rejects.toEqual(InternalError())
  })

  it("Throw an error if user is undefined", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )

    await expect(signupService.signup()).rejects.toEqual(
      InvalidParamError("user")
    )
  })

  it("Throw an error if user.firstName is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
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
      tokenGenerator,
      encrypter
    )
    const user = { firstName: "John" }
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("lastName")
    )
  })

  it("Throw an error if user.email is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )
    const user = { firstName: "John", lastName: "McLane" }
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("email")
    )
  })

  it("Throw an error if user.password is empty", async () => {
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com"
    }
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("password")
    )
  })

  it("Throw an error if user.email is not valid", async () => {
    const emailValidator = makeEmailValidator({ isValid: false })
    const signupService = makeSignupService(
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    )
    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "invalid_email",
      password: "any_password"
    }
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("email")
    )
  })

  UserRepositoryContract(makeUserRepository())
})
