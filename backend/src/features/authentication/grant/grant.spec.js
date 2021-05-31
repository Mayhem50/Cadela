import { jest } from "@jest/globals"
import { InternalError, InvalidParamError } from "@utils/errors"
import { makeGrantService } from "./grant-service"
import { TokenDecoderContract, USER_ID, TOKEN } from "./token-decoder.contract"

const makeTokenDecoder = (isValid = true) => {
  const decode = jest.fn(async (token) => {
    if (!token || !isValid) {
      throw InternalError()
    }
    return USER_ID
  })

  return { decode }
}

const tokenDecoder = makeTokenDecoder()

describe("Grant user", () => {
  it("Return user id if token is valid", async () => {
    const grantService = makeGrantService(tokenDecoder)
    const response = await grantService.grant(TOKEN)
    expect(tokenDecoder.decode).toBeCalledWith(TOKEN)
    expect(response.userId).toEqual(USER_ID)
  })

  it("Throw internal error if fail to decode", async () => {
    const tokenDecoder = makeTokenDecoder(false)
    const grantService = makeGrantService(tokenDecoder)
    await expect(grantService.grant(TOKEN)).rejects.toEqual(InternalError())
  })

  it("Throw internal error if no tokenDecoder injected", async () => {
    const grantService = makeGrantService()
    await expect(grantService.grant(TOKEN)).rejects.toEqual(InternalError())
  })

  it("Throw invalid parameter error if fail no token", async () => {
    const grantService = makeGrantService(tokenDecoder)
    await expect(grantService.grant()).rejects.toEqual(
      InvalidParamError("token")
    )
  })

  it("Throw invalid parameter error if fail token is empty string", async () => {
    const grantService = makeGrantService(tokenDecoder)
    await expect(grantService.grant("")).rejects.toEqual(
      InvalidParamError("token")
    )
  })

  TokenDecoderContract(makeTokenDecoder())
})
