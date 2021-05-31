import { jest, beforeEach } from "@jest/globals"
import { InternalError, InvalidParamError } from "@utils/errors"

const TOKEN = "any_token"
const USER_ID = "any_user_id"

const makeGrantService = (encrypter) => {
  const grant = async (token) => {
    if (!encrypter) {
      throw InternalError()
    }
    if (!token) {
      throw InvalidParamError("token")
    }
    const userId = await encrypter.descrypt(token)
    return { userId }
  }

  return { grant }
}

const makeEncrypter = (isValid = true) => {
  const descrypt = jest.fn(async (token) => {
    if (!isValid) {
      throw InternalError()
    }
    return USER_ID
  })

  return { descrypt }
}

const encrypter = makeEncrypter()

describe("Grant user", () => {
  it("Return user id if token is valid", async () => {
    const grantService = makeGrantService(encrypter)
    const response = await grantService.grant(TOKEN)
    expect(encrypter.descrypt).toBeCalledWith(TOKEN)
    expect(response.userId).toEqual(USER_ID)
  })

  it("Throw internal error if fail to decode", async () => {
    const encrypter = makeEncrypter(false)
    const grantService = makeGrantService(encrypter)
    await expect(grantService.grant(TOKEN)).rejects.toEqual(InternalError())
  })

  it("Throw internal error if no encrypter injected", async () => {
    const grantService = makeGrantService()
    await expect(grantService.grant(TOKEN)).rejects.toEqual(InternalError())
  })

  it("Throw invalid parameter error if fail no token", async () => {
    const grantService = makeGrantService(encrypter)
    await expect(grantService.grant()).rejects.toEqual(
      InvalidParamError("token")
    )
  })

  it("Throw invalid parameter error if fail token is empty string", async () => {
    const grantService = makeGrantService(encrypter)
    await expect(grantService.grant("")).rejects.toEqual(
      InvalidParamError("token")
    )
  })
})
