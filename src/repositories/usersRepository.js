const {ObjectId} = require("mongodb");
module.exports = class {
  mongoClient
  app
  collectionName = 'users'
  /**
   * 
   * @param {import("express").Application} app 
   * @param {import("mongodb").MongoClient} mongoClient
   */
  constructor(app, mongoClient) {
    this.mongoClient = mongoClient
    this.app = app
  }

  async insertUser(user) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const usersCollection = database.collection(this.collectionName)
      const result = await usersCollection.insertOne(user)
      return result.insertedId
    } catch (err) {
      throw err
    }
  }

  async findUser(filter, options = {}) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const usersCollection = database.collection(this.collectionName)
      return await usersCollection.findOne(filter, options)
    } catch (err) {
      throw err
    }
  }

  async getUsersPg(filter, options, page){
    try {
      const limit = 5;
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db("mywallapop")
      const usersCollection = database.collection(this.collectionName)
      const usersCollectionCount = await usersCollection.count()
      const cursor = usersCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
      const users = await cursor.toArray()
      const result = {users: users, total: usersCollectionCount}
      return result
    } catch (error) {
      throw error
    }
  }

  async deleteUsers(selectedUsers){
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const usersCollection = database.collection(this.collectionName)
      const  result = await usersCollection.deleteMany({email: {$in: selectedUsers}})
      return result.deletedCount
    } catch (err) {
      throw err
    }
  }


}