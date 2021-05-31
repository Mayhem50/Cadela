import { mongDbUserRepository } from "../shared/user-repository"
import { makeEmailValidator } from "../shared/email-validator"
import { makeEncrypter } from "../shared/encrypter"
import { makeTokenGenerator } from "../shared/token-generator"
import { makeHandler } from "./request-handler"
import { makeSigninService } from "./signin-service"

const emailValidator = makeEmailValidator()
const tokenGenerator = makeTokenGenerator()
const encrypter = makeEncrypter()

const signinService = makeSigninService({
  userRepository: mongDbUserRepository,
  emailValidator,
  tokenGenerator,
  encrypter
})

const signinHandler = makeHandler(signinService)

export { signinHandler }
