import { InvalidParamError, throwAppError } from "@utils/errors"

export const makeGrantService = (tokenDecoder) => {
  const grant = async (token) => {
    try {
      if (!token) {
        throw InvalidParamError("token")
      }
      const userId = await tokenDecoder.decode(token)
      return { userId }
    } catch (error) {
      throwAppError(error)
    }
  }

  return { grant }
}
