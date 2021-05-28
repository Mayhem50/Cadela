export const TokenGeneratorContract = (tokenGeneratorUnderTest) => {
  describe("Token Generator Contract", () => {
    it("Generate a unique token based on userId", () => {
      const token1 = tokenGeneratorUnderTest.generate("user_id_1")
      const token2 = tokenGeneratorUnderTest.generate("user_id_2")
      expect(token1).not.toBe(token2)
    })
  })
}
