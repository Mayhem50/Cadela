import { InvalidParamError, throwAppError } from "@utils/errors"

export const makeStoreService = ({ dataRepository } = {}) => {
  const store = async (userId, data) => {
    try {
      if (!userId) {
        throw InvalidParamError("userId")
      }
      if (!data) {
        throw InvalidParamError("data")
      }
      await dataRepository.save(userId, data)
      return { body: { success: true } }
    } catch (error) {
      throwAppError(error)
    }
  }

  return { store }
}
