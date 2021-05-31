export const makeCallback = (handler) => (req, res) => {
  handler
    .execute(req.body)
    .then((response) => res.status(response.statusCode).json(response.body))
    .catch((response) => res.status(response.statusCode).json(response.body))
}
