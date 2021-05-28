import { InternalError, InvalidParamError } from "../../shared/errors"

function validateInput(email, password, emailValidator) {
  if (!email) {
    throw InvalidParamError("email")
  }
  if (!password) {
    throw InvalidParamError("password")
  }

  if (!emailValidator.valid(email)) {
    throw InvalidParamError("email")
  }
}

async function checkPassword(encrypter, password, foundUser) {
  const isRightPassword = await encrypter.compare(password, foundUser.password)

  if (!isRightPassword) {
    throw InternalError("wrong email/password")
  }
}

export const makeSigninService = ({
  emailValidator,
  userRepository,
  encrypter,
  tokenGenerator
} = {}) => {
  const sign = async (credential = {}) => {
    try {
      const { email, password } = credential
      validateInput(email, password, emailValidator)

      const foundUser = await userRepository.getByEmail(email)

      if (!foundUser) {
        throw InternalError("user not found")
      }

      await checkPassword(encrypter, password, foundUser)

      const token = tokenGenerator.generate(foundUser.id)

      return {
        body: { token, user: { ...foundUser } }
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
