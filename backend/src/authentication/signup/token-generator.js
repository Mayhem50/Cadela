import Jwt from "jsonwebtoken"

export const makeTokenGenerator = () => {
  const generate = (userId) => {
    return Jwt.sign(userId, "jwt_secret")
  }

  return { generate }
}
