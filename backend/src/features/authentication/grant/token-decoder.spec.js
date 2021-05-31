import { jest } from "@jest/globals"
import { InternalError } from "@utils/errors"
import { makeTokenDecoder } from "./token-decoder"
import { TokenDecoderContract } from "./token-decoder.contract"

const makeMockTokenDecoder = () => {
  const tokenDecoder = makeTokenDecoder()
  tokenDecoder.decode = async (token, secret) => {
    if (!token) {
      throw InternalError()
    }
    return "any_user_id"
  }
  return tokenDecoder
}

TokenDecoderContract(makeMockTokenDecoder())
