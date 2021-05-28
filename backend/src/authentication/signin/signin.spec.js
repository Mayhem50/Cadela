import { jest, beforeEach } from "@jest/globals"
import { InternalError, InvalidParamError } from "../../shared/errors"
import { makeSigninService } from "./signin-service"
import {
  UserRepositoryContract,
  USER_EMAIL,
  USER_ID
} from "./user-repository.contract"

const makeEmailValidator = (isValid = true) => {
  const valid = () => {
    return isValid
  }
  return { valid }
}

const makeUserRepository = (found = true) => {
  const getByEmail = async (email) => {
    return found ? { id: USER_ID, password: "any_password", email } : undefined
  }
  return { getByEmail }
}

const makeEncrypter = (isRightPassword = true) => {
  const compare = async (password, hash) => {
    return isRightPassword
  }

  return { compare }
}

const makeTokenGenerator = () => {
  const generate = jest.fn((userId) => {
    return userId
  })
  return { generate }
}

const emailValidator = makeEmailValidator()
const userRepository = makeUserRepository()
const encrypter = makeEncrypter()
const tokenGenerator = makeTokenGenerator()

describe("Signin", () => {
  it("Sign a user when email & password are provided and return a token", async () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter,
      tokenGenerator
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    const ret = await signinService.sign(credential)
    expect(ret.body).toHaveProperty("token")
    expect(ret.body.token).not.toBe("")
    expect(ret.body.token).toBeDefined()
  })

  it("Fail if no credential provided", async () => {
    const signinService = makeSigninService()
    await expect(signinService.sign()).rejects.toEqual(
      InvalidParamError("credential")
    )
  })

  it("Fail if no credential does not contain email", async () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InvalidParamError("email")
    )
  })

  it("Fail if no credential does not contain password", async () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InvalidParamError("password")
    )
  })

  it("Fail if email provided is not valid", async () => {
    const emailValidator = makeEmailValidator(false)
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InvalidParamError("email")
    )
  })

  it("Fail if email validator is not provided", async () => {
    const signinService = makeSigninService()
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InternalError()
    )
  })

  it("Fail if email does not exit in db", async () => {
    const userRepository = makeUserRepository(false)
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InternalError("user not found")
    )
  })

  it("Fail if user repository is not provided", async () => {
    const signinService = makeSigninService(emailValidator)
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InternalError()
    )
  })

  it("Fail if wrong password provided", async () => {
    const encrypter = makeEncrypter(false)
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InternalError("wrong email/password")
    )
  })

  it("Fail if encrypter is not provided", async () => {
    const signinService = makeSigninService(emailValidator, userRepository)
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InternalError()
    )
  })

  it("Use token generator to create token", async () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter,
      tokenGenerator
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }

    await signinService.sign(credential)

    expect(tokenGenerator.generate).toBeCalledWith(USER_ID)
  })

  it("Fail if token generator is not provided", async () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    await expect(signinService.sign(credential)).rejects.toEqual(
      InternalError()
    )
  })

  UserRepositoryContract(makeUserRepository(true))
  describe("Token Generator Contract", () => {
    it("Generate a unique token based on userId", () => {
      const token1 = tokenGenerator.generate("user_id_1")
      const token2 = tokenGenerator.generate("user_id_2")
      expect(token1).not.toBe(token2)
    })
  })
})
