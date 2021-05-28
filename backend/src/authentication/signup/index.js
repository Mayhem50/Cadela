import { mongDbUserRepository } from "../shared/user-repository"
import { makeEmailValidator } from "./email-validator"
import { makeEncrypter } from "../shared/encrypter"
import { makeTokenGenerator } from "../shared/token-generator"
import { makeSignupService } from "./signup-service"
import { makeHandler } from "./request-handler"

const emailValidator = makeEmailValidator()
const tokenGenerator = makeTokenGenerator()
const encrypter = makeEncrypter()

const signupService = makeSignupService(
  mongDbUserRepository,
  emailValidator,
  tokenGenerator,
  encrypter
)

const signupHandler = makeHandler(signupService)

export { signupHandler }
