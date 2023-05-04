const { hashSync } = require("bcrypt")

/**
 * @param {import("express").Application} app
 * @param {import("mongodb").MongoClient} mongoClient
 * @param {import("../repositories/usersRepository")} usersRepo 
 * @param {import("../repositories/offersRepository")} offersRepo 
 * @param {import("../repositories/logsRepository")} logsRepo 
 */
module.exports = async function(app, mongoClient, usersRepo, offersRepo, logsRepo,conversationRepo) {
  await dropDatabase(app, mongoClient)
  addUsers(usersRepo)
  addOffers(offersRepo)
  addConversations(conversationRepo)
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
async function addOffers(offersRepo)
{
  offersRepo.insertOffer( {
    title: "Silla",
    description: "Silla fabricada con madera de castaño",
    price: 150,
    date: new Date(),
    seller: "usuario02@email.com",
    available: true,
    featured: true
  })

  offersRepo.insertOffer( {
    title: "Mesa",
    description: "Mesa redonda fabricada con madera de roble",
    price: 350,
    date: new Date(),
    seller: "usuario01@email.com",
    available: false,
    featured: false
  })
}

async function addConversations(conversationsRepo)
{
  conversationsRepo.insertConversation( {
    buyer: "user02@email.com",
    offer: "Mesa",
    seller:"user01@email.com",
    message: [1,2,3],
    date: new Date()
  })

  conversationsRepo.insertConversation( {
    buyer: "user03@email.com",
    offer: "Silla",
    seller:"user01@email.com",
    message: [1,2],
    date: new Date()
  })

  conversationsRepo.insertConversation( {
    buyer: "user03@email.com",
    offer: "Sofa",
    seller:"user02@email.com",
    message: [1,2,3],
    date: new Date()
  })

  conversationsRepo.insertConversation( {
    buyer: "user01@email.com",
    offer: "Sofa",
    seller:"user02@email.com",
    message: [1,2,3],
    date: new Date()
  })

}