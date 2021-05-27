export const makeHandler = (signupService) => {
  const execute = async (request) => {
    try {
      const token = await signupService.signup(request.user)

      return {
        statusCode: 200,
        body: { token }
      }
    } catch (error) {
      return {
        statusCode: error.name === "InvalidParamError" ? 400 : 500,
        body: { error }
      }
    }
  }

  return { execute }
}
