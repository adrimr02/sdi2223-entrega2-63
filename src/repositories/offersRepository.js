module.exports = class {
  mongoClient
  app
  collectionName = 'offers'
  /**
   * 
   * @param {import("express").Application} app 
   * @param {import("mongodb").MongoClient} mongoClient
   */
  constructor(app, mongoClient) {
    this.mongoClient = mongoClient
    this.app = app
  }

  async insertOffer(offer) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const usersCollection = database.collection(this.collectionName)
      const result = await usersCollection.insertOne(offer)
      return result.insertedId
    } catch (err) {
      throw err
    }
  }

}