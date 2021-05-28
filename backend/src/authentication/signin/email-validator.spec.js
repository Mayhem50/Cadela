import { EmailValidatorContract } from "./email-validator.contract"
import { makeEmailValidator } from "./email-validator"

describe("Email Validator from email-validator", () => {
  EmailValidatorContract(makeEmailValidator())
})
