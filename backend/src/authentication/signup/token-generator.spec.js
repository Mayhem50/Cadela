import Jwt from "jsonwebtoken"
import { TokenGeneratorContract } from "./token-generator.contract"

const makeTokenGenerator = () => {
  const generate = (userId) => {
    return Jwt.sign(userId, "jwt_secret")
  }

  return { generate }
}

describe("JWT Token Generator", () => {
  TokenGeneratorContract(makeTokenGenerator())
})
