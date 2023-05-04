const { ObjectId } = require("mongodb")

module.exports = class {
    mongoClient
    app
    collectionName = 'conversations'


    constructor(app, mongoClient) {
        this.mongoClient = mongoClient
        this.app = app
    }

    async insertConversation(conversation) {
        try {
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db('mywallapop')
            const conversationsCollection = database.collection(this.collectionName)
            const result = await conversationsCollection.insertOne(conversation)
            return result.insertedId
        } catch (err) {
            throw err
        }
    }

    async findConversation(filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db('mywallapop')
            const conversationsCollection = database.collection(this.collectionName)
            return await conversationsCollection.find(filter, options).toArray();
        } catch(error) {
            throw error
        }
    }

    async deleteConversation(id) {
        try {
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db('mywallapop')
            const conversationsCollection = database.collection(this.collectionName)
            return await conversationsCollection.findOneAndDelete({ _id: new ObjectId(id) })
        } catch (err) {
            throw err
        }
    }
}