import { InternalError, InvalidParamError } from "../../shared/errors"

export const HttpPostHandlerContract = (
  serviceName,
  {
    handlerUnderTestFactory,
    serviceFactory,
    serviceFactoryParameters,
    defaultPayload
  }
) =>
  describe(`${serviceName} Http POST Handler`, () => {
    let usecaseService

    beforeEach(() => {
      usecaseService = serviceFactory(serviceFactoryParameters)
    })

    it("Return 200 and a token if it succes when a complete user is sent", async () => {
      const handler = handlerUnderTestFactory(usecaseService)

      const request = defaultPayload

      const response = await handler.execute(request)

      expect(response.body).toBeDefined()
      expect(response.body.token).toBeDefined()
      expect(response.statusCode).toBe(200)
    })

    it("Return 400 if service throw an InvalidParamError", async () => {
      const handler = handlerUnderTestFactory(usecaseService)

      const key = Object.keys(defaultPayload)[0]
      const request = { credential: undefined }
      const response = await handler.execute(request)

      expect(response.body).toBeDefined()
      expect(response.body.error).toBeDefined()
      expect(response.body.error).toEqual(
        expect.objectContaining({ name: InvalidParamError().name })
      )
      expect(response.statusCode).toBe(400)
    })

    it("Return 500 if service throw an InternalError", async () => {
      const signinService = serviceFactory()
      const handler = handlerUnderTestFactory(signinService)

      const request = defaultPayload
      const response = await handler.execute(request)

      expect(response.body).toBeDefined()
      expect(response.body.error).toBeDefined()
      expect(response.body.error).toEqual(InternalError())
      expect(response.statusCode).toBe(500)
    })
  })
