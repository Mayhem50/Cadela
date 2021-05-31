import { InternalError } from "@utils/errors"
import Jwt from "jsonwebtoken"

export const makeTokenDecoder = () => {
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
