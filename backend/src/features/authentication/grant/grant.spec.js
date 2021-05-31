import { jest, beforeEach } from "@jest/globals"

const TOKEN = "any_token"
const USER_ID = "any_user_id"

const makeGrantService = (encrypter) => {
  const grant = async (token) => {
    const userId = await encrypter.descrypt(token)
    return { userId }
  }

  return { grant }
}

const makeEncrypter = () => {
  const descrypt = jest.fn(async (token) => {
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
})
