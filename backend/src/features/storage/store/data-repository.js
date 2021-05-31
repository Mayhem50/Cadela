export const makeDataRepository = (client) => {
  const store = async (userId, data) => {
    const collection = await client
      .db(process.env.DB_NAME)
      .collection(`${userId}-datas`)
    await collection.insertOne(data)
  }

  return { store }
}
