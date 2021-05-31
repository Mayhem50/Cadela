import { InvalidParamError } from "../shared/errors"

const makeStoreService = () => {
  const store = (data) => {
    if (!data) {
      throw InvalidParamError("data")
    }
    return { body: { success: true } }
  }

  return { store }
}

describe("Storage", () => {
  it("Return success true if it success to store data", () => {
    const storageService = makeStoreService()
    const response = storageService.store({})
    expect(response.body.success).toBe(true)
  })

  it("Throw  an invalid parameter error if no data provided", () => {
    const storageService = makeStoreService()
    expect(storageService.store).toThrow(InvalidParamError("data"))
  })
})
