export const EncrypterContract = (encrypterUnderTest) =>
  describe("Encrypter Contract", () => {
    it("Always encrypt password the same", async () => {
      const hash1 = await encrypterUnderTest.encrypt("any_password")
      const hash2 = await encrypterUnderTest.encrypt("any_password")
      expect(hash1).toBe(hash2)
    })
  })
