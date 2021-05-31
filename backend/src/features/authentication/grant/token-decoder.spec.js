import { jest } from "@jest/globals"
import { InternalError } from "@utils/errors"
import Jwt from "jsonwebtoken"
import { TokenDecoderContract } from "./token-decoder.contract"

const makeTokenDecoder = () => {
  const decode = async (token) => {
    try {
      const { userId } = Jwt.decode(token, process.env.JWT_SECRET)
      return userId
    } catch (error) {
      throw InternalError()
    }
  }

  return { decode }
}

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
