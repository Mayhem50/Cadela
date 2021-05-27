import EmailValidator from "email-validator"
import { EmailValidatorContract } from "./email-validator.contract"

const makeEmailValidator = () => {
  const valid = (email) => {
    return EmailValidator.validate(email)
  }
  return { valid }
}

describe("Email Validator from email-validator", () => {
  EmailValidatorContract(makeEmailValidator())
})
