import { EncrypterContract } from "./encrypter.contract"
import { makeEncrypter } from "../shared/encrypter"

describe("BcryptJs Encrypter", () => {
  EncrypterContract(makeEncrypter())
})
