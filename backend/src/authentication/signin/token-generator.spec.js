import { makeTokenGenerator } from "../shared/token-generator"
import { TokenGeneratorContract } from "./token-generator.contract"

describe("JWT Token Generator", () => {
  TokenGeneratorContract(makeTokenGenerator())
})
