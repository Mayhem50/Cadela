export const makeCallback = (handler) => (req, res) => {
  handler
    .execute(req.body)
    .then((response) => res.status(response.statusCode).json(response.body))
    .catch((response) => res.status(response.statusCode).json(response.body))
}

export const makeAuthCallback = (grantService, handler) => async (req, res) => {
  try {
    const token = req.headers["authorization"].split(" ")[1]
    const { userId } = await grantService.grant(token)
    if (!userId) {
      return res.status(400).json({ message: "Unautorized" })
    }

    req.body = { ...req.body, userId }
    return makeCallback(handler)(req, res)
  } catch (error) {
    res.status(500).json(error)
  }
}
