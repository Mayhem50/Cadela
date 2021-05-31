const makeStoreService = () => {
  const store = (data) => {
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
})
