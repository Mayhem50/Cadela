import { jest, beforeEach } from "@jest/globals"
import { InvalidParamError } from "../shared/errors"

const RAW_DATA = {}

const makeStoreService = (dataRepository) => {
  const store = (data) => {
    if (!data) {
      throw InvalidParamError("data")
    }
    dataRepository.save(data)
    return { body: { success: true } }
  }

  return { store }
}

const makeRepository = () => {
  const save = jest.fn((data) => {})
  return { save }
}

const dataRepository = makeRepository()

describe("Storage", () => {
  it("Return success true if it success to store data", () => {
    const storageService = makeStoreService(dataRepository)
    const response = storageService.store(RAW_DATA)
    expect(dataRepository.save).toBeCalledWith(RAW_DATA)
    expect(response.body.success).toBe(true)
  })

  it("Throw  an invalid parameter error if no data provided", () => {
    const storageService = makeStoreService()
    expect(storageService.store).toThrow(InvalidParamError("data"))
  })
})
