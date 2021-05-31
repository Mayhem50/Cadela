import { HttpResponse } from "@utils/http-response"

export const makeHandler = (storageService) => {
  const execute = async (request) => {
    try {
      const response = await storageService.store(request.userId, request.data)

      return HttpResponse.ok(response.body)
    } catch (error) {
      return error.name === "InvalidParamError"
        ? HttpResponse.requestError({ error })
        : HttpResponse.internalError({ error })
    }
  }

  return { execute }
}
