import bcrypt from "bcryptjs"
import { EncrypterContract } from "./encrypter.contract"

const makeEncrypter = () => {
  const encrypt = async (password) => {
    return bcrypt.hashSync(password, 10)
  }

  const compare = async (password, hash) => {
    return bcrypt.compareSync(password, hash)
  }

  return { encrypt, compare }
}

describe("BcryptJs Encrypter", () => {
  EncrypterContract(makeEncrypter())
})
