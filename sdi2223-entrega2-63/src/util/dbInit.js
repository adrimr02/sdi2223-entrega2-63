const { hashSync } = require("bcrypt")

/**
 * @param {import("express").Application} app
 * @param {import("mongodb").MongoClient} mongoClient
 * @param {import("../repositories/usersRepository")} usersRepo
 * @param {import("../repositories/offersRepository")} offersRepo
 * @param {import("../repositories/logsRepository")} logsRepo
  * @param {import("../repositories/conversationsRepository")} conversationRepo
  * @param {import("../repositories/messageRepository")} messageRepo
 */
module.exports = async function(app, mongoClient, usersRepo, offersRepo, logsRepo, conversationRepo, messageRepo) {
  await dropDatabase(app, mongoClient)
  await addUsers(usersRepo)
  await addOffers(offersRepo)
  await addConversations(conversationRepo, messageRepo)
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
async function addUsers(usersRepo) {
  usersRepo.insertUser({
    email: 'admin@email.com',
    name: 'admin',
    lastname: 'user',
    birthday: new Date('12/2/1998'),
    password: hashSync('admin', 10),
    wallet: 100,
    userType: 'admin'
  })

  const wantedUsers = 15
  for (let i = 1; i <= wantedUsers; i++) {
    const userPrefix = `${ i >= 10 ? i : '0' + i }`
    await usersRepo.insertUser({
      email: `user${userPrefix}@email.com`,
      name: 'user',
      lastname: `${userPrefix}`,
      birthday: new Date('12/2/1998'),
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
  await offersRepo.insertOffer( {
    title: "Silla",
    description: "Silla fabricada con madera de castaño",
    price: 150,
    date: new Date(),
    seller: "user02@email.com",
    available: true,
    featured: true
  })

  await offersRepo.insertOffer( {
    title: "Mesa",
    description: "Mesa redonda fabricada con madera de roble",
    price: 350,
    date: new Date(),
    seller: "user01@email.com",
    available: false,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Sofá",
    description: "Sofá de cuero negro",
    price: 500,
    date: new Date(),
    seller: "user03@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Lámpara",
    description: "Lámpara de pie moderna",
    price: 100,
    date: new Date(),
    seller: "user04@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Alfombra",
    description: "Alfombra de lana tejida a mano",
    price: 200,
    date: new Date(),
    seller: "user04@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Mesa grande",
    description: "Mesa de comedor extensible de roble",
    price: 800,
    date: new Date(),
    seller: "user05@email.com",
    available: false,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Cuadro",
    description: "Cuadro abstracto pintado a mano",
    price: 300,
    date: new Date(),
    seller: "user06@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Sillón",
    description: "Sillón orejero tapizado en tela de lino",
    price: 400,
    date: new Date(),
    seller: "user07@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Juego de café",
    description: "Juego de café de porcelana china",
    price: 50,
    date: new Date(),
    seller: "user08@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Cama grande",
    description: "Cama de matrimonio con cabecero de hierro forjado",
    price: 600,
    date: new Date(),
    seller: "user09@email.com",
    available: false,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Cortina",
    description: "Cortina de seda estampada",
    price: 150,
    date: new Date(),
    seller: "user10@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Jarrón",
    description: "Jarrón de cristal de Murano",
    price: 250,
    date: new Date(),
    seller: "user11@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Estantería",
    description: "Estantería de madera maciza",
    price: 200,
    date: new Date(),
    seller: "user12@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Cuadro pequeño",
    description: "Cuadro al óleo de paisaje",
    price: 150,
    date: new Date(),
    seller: "user13@email.com",
    available: false,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Sofá cama",
    description: "Sofá cama de piel blanca",
    price: 600,
    date: new Date(),
    seller: "user14@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Lámpara de techo",
    description: "Lámpara de techo de diseño",
    price: 80,
    date: new Date(),
    seller: "user15@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Silla grande",
    description: "Silla de escritorio ergonómica",
    price: 100,
    date: new Date(),
    seller: "user01@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Mesa pequeña",
    description: "Mesa de centro de cristal y acero",
    price: 250,
    date: new Date(),
    seller: "user02@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Escultura",
    description: "Escultura de bronce abstracta",
    price: 350,
    date: new Date(),
    seller: "user03@email.com",
    available: false,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Alfombra persa",
    description: "Alfombra persa de seda",
    price: 800,
    date: new Date(),
    seller: "user04@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Vitrina",
    description: "Vitrina de madera y vidrio",
    price: 450,
    date: new Date(),
    seller: "user05@email.com",
    available: true,
    featured: false
  })

  await offersRepo.insertOffer( {
    title: "Espejo",
    description: "Espejo con marco dorado",
    price: 120,
    date: new Date(),
    seller: "user06@email.com",
    available: true,
    featured: false
  })

}

async function addConversations(conversationsRepo, messageRepo)
{
  await conversationsRepo.insertConversation( {
    buyer: "user02@email.com",
    offer: "Mesa",
    seller:"user01@email.com",
    date: new Date()
  })

  await conversationsRepo.insertConversation( {
    buyer: "user03@email.com",
    offer: "Silla",
    seller:"user01@email.com",
    date: new Date()
  })

  await conversationsRepo.insertConversation( {
    buyer: "user03@email.com",
    offer: "Sofa",
    seller:"user02@email.com",
    date: new Date()
  })

  await conversationsRepo.insertConversation( {
    buyer: "user01@email.com",
    offer: "Sofa",
    seller:"user02@email.com",
    date: new Date()
  })

  await messageRepo.insertMessage({
    buyer: "user01@email.com",
    offer: "Sofa",
    seller:"user02@email.com",
    writer:"user01@email.com",
    content:"Hola que tal?",
    date: new Date(),
    read: true

  })

  await messageRepo.insertMessage({
    buyer: "user01@email.com",
    offer: "Sofa",
    seller:"user02@email.com",
    writer:"user02@email.com",
    content:"Muy bien, y tu?",
    date: new Date(),
    read: true
  })

  await messageRepo.insertMessage({
    buyer: "user03@email.com",
    offer: "Silla",
    seller:"user01@email.com",
    writer:"user03@email.com",
    content:"Buenas, estaba intersado en esta silla",
    date: new Date(),
    read: true
  })

}