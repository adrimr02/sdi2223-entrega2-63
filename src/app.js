// Import dependencies here
const express = require('express')
const { MongoClient } = require('mongodb')
const createError = require('http-errors')
const path = require('path')
const cookieParser = require('cookie-parser')
const logger = require('morgan')
const expressSession = require('express-session')
const jwt = require('jsonwebtoken')
const bodyParser = require('body-parser')
/*const {createLogger, format, transports} = require("winston");
let loggerW = require('winston-mongodb');*/

// Import proyect files here
const UsersRepository = require('./repositories/usersRepository')
const OffersRepository = require('./repositories/offersRepository')
const LogsRepository = require('./repositories/logsRepository')
const ConversationsRepository = require('./repositories/conversationsRepository')
const MessageRepository = require('./repositories/messageRepository')

const userSessionRouter = require('./routes/userSessionRouter')
const adminSessionRouter = require('./routes/adminSessionRouter')
const userNoSessionRouter = require('./routes/userNoSessionRouter')
const userTokenRouter = require('./routes/api/userTokenRouter')

const app = express()

//Setting global variables
app.set('jwt', jwt)
app.set('clave', 'secreto')

// Initialize Repositories here
const uri = "mongodb://127.0.0.1:27017"
app.set('mongouri', uri)
const usersRepository = new UsersRepository(app, MongoClient)
const offersRepository = new OffersRepository(app, MongoClient)
const logsRepository = new LogsRepository(app, MongoClient)
const conversationRepository = new ConversationsRepository(app, MongoClient)
const messageRepository = new MessageRepository(app,MongoClient)

require('./util/dbInit')(app, MongoClient, usersRepository, offersRepository, logsRepository, conversationRepository,messageRepository)
const loggerW = require("./util/logger")

// Initialize logger
/*loggerW = createLogger({
  transports: [
    new transports.MongoDB({
      db: 'mongodb://127.0.0.1:27017/mywallapop',
      level: 'info',
      collection: 'logs',
      format: format.combine(format.timestamp(), format.json()),
      options: { useUnifiedTopology: true }
    })
  ]
});*/

// View engine setup
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'twig')

// Middlewares setup
app.use(logger('dev'))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(expressSession({
  secret: 'abcdefg',
  resave: true,
  saveUninitialized: true
}))
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(logMiddleware);

//Protect Routes here
app.use('/signup', userNoSessionRouter)
app.use('/login', userNoSessionRouter)
app.use('/logout', userSessionRouter)
//app.use('/logout', adminSessionRouter)
app.use('/offers/*', userSessionRouter)
app.use('/shop', userSessionRouter)
app.use('/users', adminSessionRouter)
app.use('/logs', adminSessionRouter)
app.use('/api/offers', userTokenRouter)
app.use('/api/conversations',userTokenRouter)


// Set static files
app.use(express.static(path.join(__dirname, '../public')))

// Import Routes here
require('./routes/users')(app, usersRepository)
require('./routes/offers')(app, offersRepository, usersRepository)
require('./routes/admin')(app, usersRepository, offersRepository, logsRepository)
require('./routes/api/authApi')(app, usersRepository)
require('./routes/api/conversationsApi')(app, conversationRepository, messageRepository)
require('./routes/api/offersApi')(app, offersRepository)

app.get('/', userNoSessionRouter, (req, res) => {
  res.render('index')
})


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404))
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  console.log(err)
  res.locals.message = err.message
  res.locals.error = req.app.get('env') === 'development' ? err : {}

  // render the error page
  res.status(err.status || 500)
  res.render('error', { user: req.session.user })
});

function logMiddleware(req, res, next) {
  loggerW.info({
    type: "PET",
    method: req.method,
    url: req.originalUrl,
    params: req.params
  });
  next();
}

module.exports = app 
