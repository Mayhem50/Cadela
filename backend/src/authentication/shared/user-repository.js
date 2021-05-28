import { client } from "./mongo-client"

export const makeUserRepository = (client) => {
  const save = async (user) => {
    const collection = await client.db(process.env.DB_NAME).collection("users")
    const result = await collection.insertOne({ ...user })
    return result.insertedId
  }

  const getByEmail = async (email) => {
    const collection = await client.db(process.env.DB_NAME).collection("users")
    const user = await collection.findOne({ email })
    user.id = user._id
    delete user._id
    return user
  }

  return { save, getByEmail }
}

export const mongDbUserRepository = makeUserRepository(client)
