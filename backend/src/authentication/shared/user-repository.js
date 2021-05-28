import { client } from "./mongo-client"

export const makeUserRepository = (client) => {
  const prettyfyUser = (user) => {
    if (user) {
      user.id = user._id
      delete user._id
    }
  }
  const save = async (user) => {
    const collection = await client.db(process.env.DB_NAME).collection("users")
    const result = await collection.insertOne({ ...user })
    return result.insertedId
  }

  const getByEmail = async (email) => {
    const collection = await client.db(process.env.DB_NAME).collection("users")
    const user = await collection.findOne({ email })
    prettyfyUser(user)
    return user
  }

  return { save, getByEmail }
}

export const mongDbUserRepository = makeUserRepository(client)
