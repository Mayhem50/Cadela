import {
  COMPLETE_USER,
  UserRepositoryContract
} from "./user-repository.contract"

import MongoDb from "mongodb"
const { MongoClient } = MongoDb

let connection
let collection

const makeMongoDbUserRepository = () => {
  const obfuscateUser = (user) => {
    delete user.password
  }

  const save = async (user) => {
    const result = await collection.insertOne(user)
    return result.insertedId
  }

  const getByEmail = async (email) => {
    const user = await collection.findOne({ email })
    user && obfuscateUser(user)
    return user
  }

  return { save, getByEmail }
}

UserRepositoryContract(
  makeMongoDbUserRepository(),
  beforeEach(async () => {
    connection = await MongoClient.connect(process.env.MONGO_URL, {
      useNewUrlParser: true,
      useUnifiedTopology: true
    })
    collection = await connection.db().collection("users")
    await collection.deleteMany({})
  }),
  afterEach(async () => {
    await connection.close()
  })
)
