import {
  COMPLETE_USER,
  UserRepositoryContract
} from "./user-repository.contract"

import MongoDb from "mongodb"
import { makeUserRepository } from "./user-repository"

const { MongoClient } = MongoDb

const client = new MongoClient(process.env.MONGO_URL, {
  useNewUrlParser: true,
  useUnifiedTopology: true
})

describe("MongoDB User Repository", () => {
  UserRepositoryContract(
    makeUserRepository(client),
    beforeEach(async () => {
      await client.connect()
      const collection = await client
        .db(process.env.DB_NAME)
        .collection("users")
      await collection.deleteMany({})
    }),
    afterAll(async () => {
      await client.close()
    }, 5000)
  )
})
