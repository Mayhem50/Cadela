import { InvalidParamError } from "../signup/invalid-param-error"

const makeSigninService = (emailValidator) => {
  const sign = (credential) => {
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

const emailValidator = makeEmailValidator()

describe("Signin", () => {
  it("Sign a user when email & password are provided and return a token", () => {
    const signinService = makeSigninService(emailValidator)
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
    const signinService = makeSigninService()
    const credential = {
      password: "any_password"
    }
    expect(() => signinService.sign(credential)).toThrow(
      InvalidParamError("email")
    )
  })

  it("Fail if no credential does not contain email", () => {
    const signinService = makeSigninService()
    const credential = {
      email: "any_email@mail.com"
    }
    expect(() => signinService.sign(credential)).toThrow(
      InvalidParamError("password")
    )
  })

  it("Fail if email provided is not valid", () => {
    const emailValidator = makeEmailValidator(false)
    const signinService = makeSigninService(emailValidator)
    const credential = {
      email: "any_email@mail.com",
      password: "any_password"
    }
    expect(() => signinService.sign(credential)).toThrow(
      InvalidParamError("email")
    )
  })
})
