import { jest, beforeEach } from "@jest/globals"
import { InternalError } from "../signup/internal-error"
import { InvalidParamError } from "../signup/invalid-param-error"

const makeSigninService = (
  emailValidator,
  userRepository,
  encrypter,
  tokenGenerator
) => {
  const sign = async (credential) => {
    try {
      if (!credential) {
        throw InvalidParamError("credential")
      }
      const { email, password } = credential
      if (!email) {
        throw InvalidParamError("email")
      }
      if (!password) {
        throw InvalidParamError("password")
      }

      if (!emailValidator.valid(email)) {
        throw InvalidParamError("email")
      }

      const foundUser = userRepository.getByEmail(email)

      if (!foundUser) {
        throw InternalError("user not found")
      }

      const isRightPassword = encrypter.compare(password, foundUser.password)

      if (!isRightPassword) {
        throw InternalError("wrong email/password")
      }

      const token = tokenGenerator.generate(foundUser.id)

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
  return { sign }
}

const makeEmailValidator = (isValid = true) => {
  const valid = () => {
    return isValid
  }
  return { valid }
}

const makeUserRepository = (found = true) => {
  const getByEmail = (email) => {
    return found ? { id: USER_ID, password: "any_password" } : undefined
  }
  return { getByEmail }
}

const makeEncrypter = (isRightPassword = true) => {
  const compare = (password, hash) => {
    return isRightPassword
  }

  return { compare }
}

const makeTokenGenerator = () => {
  const generate = jest.fn((userId) => {
    return "any_token"
  })
  return { generate }
}

const emailValidator = makeEmailValidator()
const userRepository = makeUserRepository()
const encrypter = makeEncrypter()
const tokenGenerator = makeTokenGenerator()
const USER_ID = "any_user_id"

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
})
