export const EncrypterContract = (encrypterUnderTest) =>
  describe("Encrypter Contract", () => {
    it("Return a non null string", async () => {
      const hash = await encrypterUnderTest.encrypt("any_password")
      expect(hash).not.toBe("")
      expect(hash).not.toBeUndefined()
    })
    it("Encrypt password and can compare it", async () => {
      const hash = await encrypterUnderTest.encrypt("any_password")
      const isSame = await encrypterUnderTest.compare("any_password", hash)
      expect(isSame).toBe(true)
    })
  })
