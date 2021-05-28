import { Router } from "express"
import { signupHandler } from "./signup"
import { signinHandler } from "./signin"
import { makeCallback } from "../shared/make-callback"

export const router = Router()

const innerRouter = Router()
innerRouter
  .post("/signup", makeCallback(signupHandler))
  .post("/signin", makeCallback(signinHandler))

router.use("/auth", innerRouter)
