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

  async updateUser(id, user) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const usersCollection = database.collection(this.collectionName)
      console.log(user)
      return await usersCollection.findOneAndUpdate({ _id: id }, user)
    } catch (err) {
      throw err
    }
  }

}