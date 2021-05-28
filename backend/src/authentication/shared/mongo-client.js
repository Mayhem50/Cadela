import MongoDb from "mongodb"
const { MongoClient } = MongoDb

export const client = new MongoClient(process.env.MONGO_URL, {
  useNewUrlParser: true,
  useUnifiedTopology: true
})

client.connect()
