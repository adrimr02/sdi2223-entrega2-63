const { ObjectId } = require("mongodb")

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
      const offersCollection = database.collection(this.collectionName)
      const result = await offersCollection.insertOne(offer)
      return result.insertedId
    } catch (err) {
      throw err
    }
  }

  async findOffer(filter, options) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const offersCollection = database.collection(this.collectionName)
      return await offersCollection.findOne(filter, options)
    } catch(error) {
      throw error
    }
  }

  async getOffersPage(filter , options, page, limit=8) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const offersCollection = database.collection(this.collectionName)
      const offers = await offersCollection.find(filter, options).skip((page - 1) * limit).limit
      (limit).toArray()
      const totalOfferCount = await offersCollection.countDocuments()
      return { offers, total: totalOfferCount }
    } catch(error) {
      throw error
    }
  }

  async updateOffer(id, offer) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const offersCollection = database.collection(this.collectionName)
      return await offersCollection.findOneAndUpdate({ _id: id }, offer)
    } catch (err) {
      throw err
    }
  }

  async deleteOffer(id) {
    try {
      const client = await this.mongoClient.connect(this.app.get('mongouri'))
      const database = client.db('mywallapop')
      const offersCollection = database.collection(this.collectionName)
      return await offersCollection.findOneAndDelete({ _id: new ObjectId(id) })
    } catch (err) {
      throw err
    }
  }

}