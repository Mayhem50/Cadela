import { jest } from "@jest/globals"

const MissingParamsError = (message) => ({
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

const makeSignupService = (userRepository) => {
  const signup = (user) => {
    if (!user) {
      throw MissingParamsError("user")
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
    const signupService = makeSignupService(userRepository)
    const user = {}
    const ret = signupService.signup(user)

    expect(userRepository.save).toHaveBeenCalledWith(user)
    expect(ret.body).toHaveProperty("token")
  })

  it("Throw an error if user is undefined", () => {
    const signupService = makeSignupService(userRepository)

    expect(signupService.signup).toThrow(MissingParamsError("user"))
  })
})
