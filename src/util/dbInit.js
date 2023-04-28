const { hashSync } = require("bcrypt")

/**
 * @param {import("express").Application} app
 * @param {import("mongodb").MongoClient} mongoClient
 * @param {import("../repositories/usersRepository")} usersRepo 
 * @param {import("../repositories/offersRepository")} offersRepo 
 * @param {import("../repositories/logsRepository")} logsRepo 
 */
module.exports = async function(app, mongoClient, usersRepo, offersRepo, logsRepo) {
  await dropDatabase(app, mongoClient)
  addUsers(usersRepo)
}

/**
 * @param {import("express").Application}
 * @param {import("mongodb").MongoClient} mongoClient 
 */
async function dropDatabase(app, mongoClient) {
  const client = await mongoClient.connect(app.get('mongouri'))
  const database = client.db('mywallapop')
  await database.dropDatabase()
}

/**
 * 
 * @param {import("../repositories/usersRepository")} usersRepo 
 */
function addUsers(usersRepo) {
  usersRepo.insertUser({
    email: 'admin@email.com',
    name: 'admin',
    lastname: 'user',
    //birthday,
    password: hashSync('admin', 10),
    wallet: 100,
    userType: 'admin'
  })

  const wantedUsers = 15
  for (let i = 1; i <= wantedUsers; i++) {
    const userPrefix = `${ i >= 10 ? i : '0' + i }`
    usersRepo.insertUser({
      email: `user${userPrefix}@email.com`,
      name: 'user',
      lastname: `${userPrefix}`,
      //birthday,
      password: hashSync(`user${userPrefix}`, 10),
      wallet: 100,
      userType: 'standard'
    })
  }
}

/**
 * 
 * @param {import("../repositories/offersRepository")} offersRepo 
 */
async function addOffers(offersRepo) {

}