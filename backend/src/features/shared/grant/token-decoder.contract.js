import { InternalError } from "@utils/errors"

export const TOKEN = "any_token"
export const USER_ID = "any_user_id"

export const TokenDecoderContract = (tokenDecoderUnderTest) =>
  describe("Token Decoder Contract", () => {
    it("Decode a valid token to a user id", async () => {
      const userId = await tokenDecoderUnderTest.decode(TOKEN)
      expect(userId).toBe(USER_ID)
    })

    it("Throw an exception if no token provided", async () => {
      await expect(tokenDecoderUnderTest.decode()).rejects.toEqual(
        InternalError()
      )
    })

    it("Throw an exception if token provided is empty", async () => {
      await expect(tokenDecoderUnderTest.decode("")).rejects.toEqual(
        InternalError()
      )
    })
  })
