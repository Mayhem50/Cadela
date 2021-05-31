import { InvalidParamError, throwAppError } from "@utils/errors"

export const makeGrantService = (encrypter) => {
  const grant = async (token) => {
    try {
      if (!token) {
        throw InvalidParamError("token")
      }
      const userId = await encrypter.decode(token)
      return { userId }
    } catch (error) {
      throwAppError(error)
    }
  }

  return { grant }
}
