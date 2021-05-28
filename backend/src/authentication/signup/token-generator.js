import Jwt from "jsonwebtoken"

export const makeTokenGenerator = () => {
  const generate = (userId) => {
    return Jwt.sign({ userId }, process.env.JWT_SECRET)
  }

  return { generate }
}
