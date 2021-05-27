import { EncrypterContract } from "./encrypter.contract"
import { makeEncrypter } from "./encrypter"

describe("BcryptJs Encrypter", () => {
  EncrypterContract(makeEncrypter())
})
