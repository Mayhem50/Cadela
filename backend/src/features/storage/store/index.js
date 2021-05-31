import { makeHandler } from "./request-handler"
import { makeStoreService } from "./store-service"
import { makeDataRepository } from "./data-repository"
import { client } from "../../shared/mongo-client"

const repository = makeDataRepository(client)
const storageService = makeStoreService({ dataRepository: repository })
const storeHandler = makeHandler(storageService)

export { storeHandler }
