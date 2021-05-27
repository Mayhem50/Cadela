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
    if (!user.firstName) {
      throw MissingParamsError("firstName")
    }
    if (!user.lastName) {
      throw MissingParamsError("lastName")
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
    const user = { firstName: "John", lastName: "McLane" }
    const ret = signupService.signup(user)

    expect(userRepository.save).toHaveBeenCalledWith(user)
    expect(ret.body).toHaveProperty("token")
  })

  it("Throw an error if user is undefined", () => {
    const signupService = makeSignupService(userRepository)

    expect(signupService.signup).toThrow(MissingParamsError("user"))
  })

  it("Throw an error if user.firstName is empty", () => {
    const signupService = makeSignupService(userRepository)
    const user = {}
    expect(() => signupService.signup(user)).toThrow(
      MissingParamsError("firstName")
    )
  })

  it("Throw an error if user.lastName is empty", () => {
    const signupService = makeSignupService(userRepository)
    const user = { firstName: "John" }
    expect(() => signupService.signup(user)).toThrow(
      MissingParamsError("lastName")
    )
  })
})
