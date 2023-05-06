module.exports = class {
    mongoClient
    app
    collectionName = 'logs'

    /**
     *
     * @param {import("express").Application} app
     * @param {import("mongodb").MongoClient} mongoClient
     */
    constructor(app, mongoClient) {
        this.mongoClient = mongoClient
        this.app = app
    }
    async getLogsPg(filter, options, page){
        try {
            const limit = 10;
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db("mywallapop")
            const logsCollection = database.collection(this.collectionName)
            const logsCollectionCount = await logsCollection.count()
            const cursor = logsCollection.find(filter, options).skip((page - 1) * limit).limit(limit)
            const logs = await cursor.toArray()
            const result = {logs: logs, total: logsCollectionCount}
            return result
        } catch (error) {
            throw error
        }
    }

    async deleteLogs(){
        try{
            const client = await this.mongoClient.connect(this.app.get('mongouri'))
            const database = client.db("mywallapop")
            const logsCollection = database.collection(this.collectionName)
            const result = await logsCollection.deleteMany({})
            return result;
        }catch (error){
            throw  error
        }
    }

}