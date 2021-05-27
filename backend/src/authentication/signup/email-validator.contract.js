export const EmailValidatorContract = (validatorUnderTest) =>
  describe("Email Validator Contract", () => {
    it("Return true when email is valid", () => {
      const isValid = validatorUnderTest.valid("any_email@mail.com")
      expect(isValid).toBe(true)
    })

    it("Return false when email is not valid", () => {
      const isValid = validatorUnderTest.valid("any_email")
      expect(isValid).toBe(false)
    })
  })
