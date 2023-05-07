const { ObjectId } = require("mongodb")

module.exports = class {
    mongoClient
    app
    collectionName = 'messages'


    constructor(app, mongoClient) {
        this.mongoClient = mongoClient
        this.app = app
    }

    async insertMessage(message) {
        try {
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db('mywallapop')
            const messagesCollection = database.collection(this.collectionName)
            const result = await messagesCollection.insertOne(message)
            return result.insertedId
        } catch (err) {
            throw err
        }
    }

    async findMessages(filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db('mywallapop')
            const messagesCollection = database.collection(this.collectionName)
            return await messagesCollection.find(filter, options).toArray()
        } catch(error) {
            throw error
        }
    }

    async deleteMessageByConversastion(idConversation) {
        try {
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db('mywallapop')
            const conversationsCollection = database.collection(this.collectionName)
            return await conversationsCollection.deleteMany({ conversation: new ObjectId(idConversation) })
        } catch (err) {
            throw err
        }
    }
}