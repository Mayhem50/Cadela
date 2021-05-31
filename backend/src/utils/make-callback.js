export const makeCallback = (handler) => (req, res) => {
  handler
    .execute(req.body)
    .then((response) => res.status(response.statusCode).json(response.body))
    .catch((response) => res.status(response.statusCode).json(response.body))
}

export const makeAuthCallback = async (grantService, handler) => (req, res) => {
  try {
    const userId = await grantService.grant(
      req.header["Authorization"].split(" ")[1]
    )
    if (!userId) {
      return res.status(400).json({ message: "Unautorized" })
    }

    return makeCallback(handler)(req, res)
  } catch (error) {}
}
