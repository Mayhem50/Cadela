import { HttpResponse } from "../../../shared/http-response"

export const makeHandler = (signinService) => {
  const execute = async (request) => {
    try {
      const response = await signinService.sign(request.credential)
      return HttpResponse.ok(response.body)
    } catch (error) {
      return error.name === "InvalidParamError"
        ? HttpResponse.requestError({ error })
        : HttpResponse.internalError({ error })
    }
  }

  return { execute }
}
