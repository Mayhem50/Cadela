import {
  InternalError,
  InvalidParamError,
  throwAppError
} from "../../shared/errors"

export const makeSigninService = ({
  emailValidator,
  userRepository,
  encrypter,
  tokenGenerator
} = {}) => {
  const sign = async (credential = {}) => {
    try {
      const { email, password } = credential

      validateInput(emailValidator, email, password)
      const foundUser = await findUser(userRepository, email)
      await checkPassword(encrypter, password, foundUser.password)

      const token = tokenGenerator.generate(foundUser.id)

      return {
        body: { token, user: { ...foundUser } }
      }
    } catch (error) {
      throwAppError(error)
    }
  }
  return { sign }
}

function validateInput(emailValidator, email, password) {
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

async function checkPassword(encrypter, password, hash) {
  const isRightPassword = await encrypter.compare(password, hash)

  if (!isRightPassword) {
    throw InternalError("wrong email/password")
  }
}

async function findUser(userRepository, email) {
  const foundUser = await userRepository.getByEmail(email)

  if (!foundUser) {
    throw InternalError("user not found")
  }
  return foundUser
}
