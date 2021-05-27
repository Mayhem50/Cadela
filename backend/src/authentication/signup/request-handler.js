import { HttpResponse } from "./http-response"

export const makeHandler = (signupService) => {
  const execute = async (request) => {
    try {
      const token = await signupService.signup(request.user)

      return HttpResponse.ok({ token })
    } catch (error) {
      return error.name === "InvalidParamError"
        ? HttpResponse.requestError({ error })
        : HttpResponse.internalError({ error })
    }
  }

  return { execute }
}
