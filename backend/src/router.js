import { Router } from "express"
import { AuthRouter } from "./features/authentication"

export const router = Router()

router.use("/api", AuthRouter)
