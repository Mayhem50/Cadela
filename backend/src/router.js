import { Router } from "express"
import { AuthRouter } from "./features/authentication"
import { StoreRouter } from "./features/storage"

export const router = Router()

router.use("/api", AuthRouter, StoreRouter)
