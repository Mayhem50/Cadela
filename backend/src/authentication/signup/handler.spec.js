import { COMPLETE_USER } from "./user-repository.contract"
import { makeSignupService } from "./signup-service"

const makeHandler = () => {
  const execute = async (request) => {
    return {
      statusCode: 200,
      body: { token: "any_token" }
    }
  }

  return { execute }
}

describe("Signup Http Handler", () => {
  it("Return 200 and a token if it succes when a complete user is sent", async () => {
    const handler = makeHandler()

    const request = { user: { ...COMPLETE_USER } }
    const response = await handler.execute(request)

    expect(response.body).toBeDefined()
    expect(response.body.token).toBeDefined()
    expect(response.statusCode).toBe(200)
  })
})
