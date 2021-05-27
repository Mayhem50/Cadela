export const HttpResponse = {
  ok: (body) => ({
    statusCode: 200,
    body
  }),
  internalError: (body) => ({
    statusCode: 500,
    body
  }),
  requestError: (body) => ({
    statusCode: 400,
    body
  })
}
