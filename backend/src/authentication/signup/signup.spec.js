import { jest } from "@jest/globals"

const makeUserRepository = () => {
  const save = jest.fn((user) => {
    return "any_token"
  })
  return { save }
}

const userRepository = makeUserRepository()

const makeSignupService = (userRepository) => {
  const signup = (user) => {
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
})
