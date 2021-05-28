import {
  InternalError,
  InvalidParamError,
  throwAppError
} from "../../shared/errors"

export const makeSignupService = ({
  userRepository,
  emailValidator,
  tokenGenerator,
  encrypter
} = {}) => {
  const signup = async (user = {}) => {
    try {
      const { firstName, lastName, email, password } = user
      validateInput(firstName, lastName, email, password, emailValidator)
      await checkIfUserExists(userRepository, email)

      const hashedPassword = await encrypter.encrypt(user.password)
      const userToSave = { ...user, password: hashedPassword }
      const userId = await userRepository.save(userToSave)
      const token = tokenGenerator.generate(userId)
      return {
        body: {
          token,
          user: {
            ...user,
            id: userId
          }
        }
      }
    } catch (error) {
      throwAppError(error)
    }
  }

  return { signup }
}

async function checkIfUserExists(userRepository, email) {
  const userFound = await userRepository.getByEmail(email)

  if (userFound) {
    throw InternalError("User already exists")
  }
}

function validateInput(firstName, lastName, email, password, emailValidator) {
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
}
