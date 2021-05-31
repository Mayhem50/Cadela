import { UserRepositoryContract, USER_EMAIL } from "./user-repository.contract"

import MongoDb from "mongodb"
import { makeUserRepository } from "../shared/user-repository"

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
      await collection.insertOne({
        password: "any_password",
        email: USER_EMAIL
      })
    }),
    afterAll(async () => {
      await client.close()
    }, 5000)
  )
})
