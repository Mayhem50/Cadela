import { InternalError } from "../signup/internal-error"
import { InvalidParamError } from "../signup/invalid-param-error"

const makeSigninService = (emailValidator, userRepository, encrypter) => {
  const sign = (credential) => {
    if (!credential) {
      throw InvalidParamError("credential")
    }
    if (!emailValidator || !userRepository || !encrypter) {
      throw InternalError()
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

    return {
      body: { token: "any_token" }
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
    return found ? { id: "any_user_id", password: "any_password" } : undefined
  }
  return { getByEmail }
}

const makeEncrypter = (isRightPassword = true) => {
  const compare = (password, hash) => {
    return isRightPassword
  }

  return { compare }
}

const emailValidator = makeEmailValidator()
const userRepository = makeUserRepository()
const encrypter = makeEncrypter()

describe("Signin", () => {
  it("Sign a user when email & password are provided and return a token", () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    const ret = signinService.sign(credential)
    expect(ret.body).toHaveProperty("token")
    expect(ret.body.token).not.toBe("")
    expect(ret.body.token).toBeDefined()
  })

  it("Fail if no credential provided", () => {
    const signinService = makeSigninService()
    expect(() => signinService.sign()).toThrow(InvalidParamError("credential"))
  })

  it("Fail if no credential does not contain email", () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      password: "any_password"
    }
    expect(() => signinService.sign(credential)).toThrow(
      InvalidParamError("email")
    )
  })

  it("Fail if no credential does not contain password", () => {
    const signinService = makeSigninService(
      emailValidator,
      userRepository,
      encrypter
    )
    const credential = {
      email: "any_email@mail.com"
    }
    expect(() => signinService.sign(credential)).toThrow(
      InvalidParamError("password")
    )
  })

  it("Fail if email provided is not valid", () => {
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
    expect(() => signinService.sign(credential)).toThrow(
      InvalidParamError("email")
    )
  })

  it("Fail if email validator is not provided", () => {
    const signinService = makeSigninService()
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    expect(() => signinService.sign(credential)).toThrow(InternalError())
  })

  it("Fail if email does not exit in db", () => {
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
    expect(() => signinService.sign(credential)).toThrow(
      InternalError("user not found")
    )
  })

  it("Fail if user repository is not provided", () => {
    const signinService = makeSigninService(emailValidator)
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    expect(() => signinService.sign(credential)).toThrow(InternalError())
  })

  it("Fail if wrong password provided", () => {
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
    expect(() => signinService.sign(credential)).toThrow(
      InternalError("wrong email/password")
    )
  })

  it("Fail if encrypter is not provided", () => {
    const encrypter = makeEncrypter(false)
    const signinService = makeSigninService(emailValidator, userRepository)
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    expect(() => signinService.sign(credential)).toThrow(InternalError())
  })
})
