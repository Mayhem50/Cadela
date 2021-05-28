import { InvalidParamError } from "../signup/invalid-param-error"

const makeSigninService = () => {
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
    return {
      body: { token: "any_token" }
    }
  }
  return { sign }
}

describe("Signin", () => {
  it("Sign a user when email & password are provided and return a token", () => {
    const signinService = makeSigninService()
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
})
