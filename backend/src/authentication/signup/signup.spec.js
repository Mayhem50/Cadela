import { jest, beforeEach } from "@jest/globals"
import { InternalError, InvalidParamError } from "../../shared/errors"
import { makeSignupService } from "./signup-service"
import {
  UserRepositoryContract,
  COMPLETE_USER
} from "./user-repository.contract"

import { EmailValidatorContract } from "./email-validator.contract"
import { TokenGeneratorContract } from "./token-generator.contract"
import { EncrypterContract } from "./encrypter.contract"
import { makeHandler } from "./request-handler"
import { HttpPostHandlerContract } from "../shared/http-handler.contract"

const makeUserRepository = () => {
  let users = []
  let userId

  const save = jest.fn(async (user) => {
    userId = users.push(user).toString()
    return userId
  })

  const getByEmail = jest.fn(async (email) => {
    return users[0]
  })

  const getUserId = () => userId

  return { save, getByEmail, getUserId }
}

const makeEmailValidator = ({ isValid } = { isValid: true }) => {
  const valid = (email) => {
    return email.includes("@")
  }

  return {
    valid
  }
}

const makeTokenGenerator = () => {
  const generate = jest.fn((userId) => {
    return `${userId}_any_token`
  })

  return { generate }
}

const makeEncrypter = () => {
  const encrypt = jest.fn(async (password) => {
    return password
  })
  const compare = jest.fn(async (password, hash) => {
    return hash === password
  })
  return { encrypt, compare }
}

describe("Signup", () => {
  let userRepository = makeUserRepository()
  let emailValidator = makeEmailValidator()
  let tokenGenerator = makeTokenGenerator()
  let encrypter = makeEncrypter()

  beforeEach(() => {
    userRepository = makeUserRepository()
    emailValidator = makeEmailValidator()
    tokenGenerator = makeTokenGenerator()
    encrypter = makeEncrypter()
  })

  it("Save user and return token", async () => {
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
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
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
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
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })

    const user = {
      firstName: "John",
      lastName: "McLane",
      email: "any_email@mail.com",
      password: "any_password"
    }

    await expect(signupService.signup(user)).rejects.toEqual(InternalError())
  })

  it("Throw an error if user is undefined", async () => {
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })

    await expect(signupService.signup()).rejects.toEqual(
      InvalidParamError("user")
    )
  })

  it("Throw an error if user.firstName is empty", async () => {
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
    const user = {}
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("firstName")
    )
  })

  it("Throw an error if user.lastName is empty", async () => {
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
    const user = { firstName: "John" }
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("lastName")
    )
  })

  it("Throw an error if user.email is empty", async () => {
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
    const user = { firstName: "John", lastName: "McLane" }
    await expect(signupService.signup(user)).rejects.toEqual(
      InvalidParamError("email")
    )
  })

  it("Throw an error if user.password is empty", async () => {
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
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
    const signupService = makeSignupService({
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    })
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

  HttpPostHandlerContract("Signup", {
    handlerUnderTestFactory: makeHandler,
    serviceFactory: makeSignupService,
    serviceFactoryParameters: {
      userRepository,
      emailValidator,
      tokenGenerator,
      encrypter
    },
    defaultPayload: { user: { ...COMPLETE_USER } }
  })
  UserRepositoryContract(makeUserRepository())
  EmailValidatorContract(makeEmailValidator())
  TokenGeneratorContract(makeTokenGenerator())
  EncrypterContract(makeEncrypter())
})
