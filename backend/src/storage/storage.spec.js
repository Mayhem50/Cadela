import { jest, beforeEach } from "@jest/globals"
import { InvalidParamError, InternalError } from "../shared/errors"

const RAW_DATA = {}

const makeStoreService = ({ dataRepository } = {}) => {
  const store = async (data) => {
    if (!dataRepository) {
      throw InternalError()
    }
    if (!data) {
      throw InvalidParamError("data")
    }
    dataRepository.save(data)
    return { body: { success: true } }
  }

  return { store }
}

const makeRepository = () => {
  const save = jest.fn(async (data) => {})
  return { save }
}

const dataRepository = makeRepository()

describe("Storage", () => {
  it("Return success true if it success to store data", async () => {
    const storageService = makeStoreService({ dataRepository })
    const response = await storageService.store(RAW_DATA)
    expect(dataRepository.save).toBeCalledWith(RAW_DATA)
    expect(response.body.success).toBe(true)
  })

  it("Throw  an invalid parameter error if no data provided", async () => {
    const storageService = makeStoreService({ dataRepository })
    await expect(storageService.store()).rejects.toEqual(
      InvalidParamError("data")
    )
  })

  it("Throw  an internal error if no repository provided", async () => {
    const storageService = makeStoreService()
    await expect(storageService.store(RAW_DATA)).rejects.toEqual(
      InternalError()
    )
  })
})
