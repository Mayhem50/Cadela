import { HttpResponse } from "../../shared/http-response"

export const makeHandler = (signupService) => {
  const execute = async (request) => {
    try {
      const response = await signupService.signup(request.user)

      return HttpResponse.ok(response.body)
    } catch (error) {
      return error.name === "InvalidParamError"
        ? HttpResponse.requestError({ error })
        : HttpResponse.internalError({ error })
    }
  }

  return { execute }
}
