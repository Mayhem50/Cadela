import { response, Router } from "express";
import { signupHandler } from "./signup";

export const router = Router()

const innerRouter = Router()
innerRouter.post('/signup', async (req, res) => {
  signupHandler.execute(req.body)
    .then(response => res.status(response.statusCode).json(response.body))
    .catch(response => res.status(response.statusCode).json(response.body))
})

router.use("/auth", innerRouter)