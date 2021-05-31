import { jest } from "@jest/globals"

const mock = jest.createMockFromModule("jsonwebtoken")

function decode(token, secretKey) {
  return "any_user_id"
}

mock.decode = decode

export default mock
