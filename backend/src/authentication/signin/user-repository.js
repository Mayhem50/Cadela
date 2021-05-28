import { client } from "../shared/mongo-client"

export const makeUserRepository = (client) => {
  const getByEmail = async (email) => {
    const collection = await client.db(process.env.DB_NAME).collection("users")
    const user = await collection.findOne({ email })
    user.id = user._id
    delete user._id
    return user
  }

  return { getByEmail }
}

export const mongDbUserRepository = makeUserRepository(client)
