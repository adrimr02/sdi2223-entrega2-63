// Import dependencies here
const express = require('express')
const { MongoClient } = require('mongodb')
const createError = require('http-errors')
const path = require('path')
const cookieParser = require('cookie-parser')
const fileUpload = require('express-fileupload')
const logger = require('morgan')
const expressSession = require('express-session')

// Import proyect files here
const UsersRepository = require('./repositories/usersRepository')
const OffersRepository = require('./repositories/offersRepository')

const userSessionRouter = require('./routes/userSessionRouter')
const userNoSessionRouter = require('./routes/userNoSessionRouter')

const app = express()

// Initialize Repositories here
const uri = "mongodb://127.0.0.1:27017"
app.set('mongouri', uri)
const usersRepository = new UsersRepository(app, MongoClient)
const offersRepository = new OffersRepository(app, MongoClient)

// View engine setup
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'twig')

// Middlewares setup
app.use(logger('dev'))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(fileUpload({
  limits: { filesize: 50 * 1024 * 1024 },
  createParentPath: true
}))
app.use(expressSession({
  secret: 'abcdefg',
  resave: true,
  saveUninitialized: true
}))

//Protect Routes here
app.use('/signup', userNoSessionRouter)
app.use('/login', userNoSessionRouter)
app.use('/logout', userSessionRouter)
app.use('/offers/*', userSessionRouter)

// Set static files
app.use(express.static(path.join(__dirname, '../public')))

// Import Routes here
require('./routes/users')(app, usersRepository)
require('./routes/offers')(app, offersRepository)

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

module.exports = app