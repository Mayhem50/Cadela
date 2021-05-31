import { Router } from "express"
import { storeHandler } from "./store"
import { makeAuthCallback } from "@utils/make-callback"
import { grantService } from "../shared/grant" // TODO: remove this depen

export const router = Router()

const innerRouter = Router()
innerRouter.post("/", makeAuthCallback(grantService, storeHandler))

router.use("/store", innerRouter)
