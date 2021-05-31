import { jest, beforeEach } from "@jest/globals"
import { InvalidParamError, InternalError, throwAppError } from "@utils/errors"

const RAW_DATA = {}
const USER_ID = 1664

const makeStoreService = ({ dataRepository } = {}) => {
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

const makeRepository = () => {
  const save = jest.fn(async (userId, data) => {})
  return { save }
}

const dataRepository = makeRepository()

describe("Storage", () => {
  it("Return success true if it success to store data", async () => {
    const storageService = makeStoreService({ dataRepository })
    const response = await storageService.store(USER_ID, RAW_DATA)
    expect(dataRepository.save).toBeCalledWith(USER_ID, RAW_DATA)
    expect(response.body.success).toBe(true)
  })

  it("Throw  an invalid parameter error if no data provided", async () => {
    const storageService = makeStoreService({ dataRepository })
    await expect(storageService.store(USER_ID)).rejects.toEqual(
      InvalidParamError("data")
    )
  })

  it("Throw  an invalid parameter error if no userId provided", async () => {
    const storageService = makeStoreService({ dataRepository })
    await expect(storageService.store()).rejects.toEqual(
      InvalidParamError("userId")
    )
  })

  it("Throw  an internal error if no repository provided", async () => {
    const storageService = makeStoreService()
    await expect(storageService.store(USER_ID, RAW_DATA)).rejects.toEqual(
      InternalError()
    )
  })
})
