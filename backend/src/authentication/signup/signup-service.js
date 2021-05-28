import { InternalError, InvalidParamError } from "../../shared/errors"

export const makeSignupService = ({
  userRepository,
  emailValidator,
  tokenGenerator,
  encrypter
} = {}) => {
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
