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

  

}