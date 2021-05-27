import EmailValidator from "email-validator"

export const makeEmailValidator = () => {
  const valid = (email) => {
    return EmailValidator.validate(email)
  }
  return { valid }
}
