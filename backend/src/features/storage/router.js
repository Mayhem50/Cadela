import { Router } from "express"
import { storeHandler } from "./store"
import { makeCallback } from "@utils/make-callback"

export const router = Router()

const innerRouter = Router()
innerRouter.post("/", makeCallback(storeHandler))

router.use("/store", innerRouter)
