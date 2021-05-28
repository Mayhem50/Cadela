import { Router } from "express"
import { AuthRouter } from "./authentication";

export const router = Router()

router.use('/api', AuthRouter)
