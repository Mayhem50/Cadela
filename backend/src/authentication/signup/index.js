import { makeUserRepository } from "./user-repository"
import { makeEmailValidator } from "./email-validator"
import { makeEncrypter } from "./encrypter"
import { makeTokenGenerator } from "./token-generator"
import { makeSignupService } from "./signup-service"

const userRepository = makeUserRepository()
const emailValidator = makeEmailValidator()
const tokenGenerator = makeTokenGenerator()
const encrypter = makeEncrypter()

const SignupService = makeSignupService(
  userRepository,
  emailValidator,
  tokenGenerator,
  encrypter
)

export { SignupService }
