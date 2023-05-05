const { hashSync, compareSync, genSaltSync } = require('bcrypt')
const loggerW = require("../util/logger")
const dateValidation = require('../util/dateValidation')
/**
 * 
 * @param {import("express").Application} app 
 * @param {import("../repositories/usersRepository")} usersRepository
 */
module.exports = function(app, usersRepository) {

  app.get('/signup', (req, res) => {
    res.render('signup.twig')
  })

  app.post('/signup', async (req, res) => {
    const { email, name, lastname, birthday, password, repeatPassword } = req.body

    const errors = []

    if (!email || !name || !lastname || !birthday || !password || !repeatPassword) {
      errors.push('Es obligatorio rellenar todos los campos.')
      res.redirect(`/signup?message=${errors.join('<br>')}&messageType=alert-danger`)
      return
    }

    const user = await usersRepository.findUser({ email })

    if (user) {
      errors.push('El email ya está en uso.')
    }

    if (password.length < 6) {
      errors.push('La contraseña debe incluir, al menos, 6 caracteres.')
    }

    if (password !== repeatPassword) {
      errors.push('Las contraseñas no coinciden.')
    }

    if (!dateValidation(birthday)) {
      errors.push('La fecha no es válida o no sigue el formato DD/MM/AAAA.')
    } else if (new Date().getTime() < new Date(birthday).getTime()) {
      errors.push('La fecha de cumpleaños no puede ser posterior a la actual.')
    }

    if (errors.length === 0) {
      const salt = genSaltSync(10)
      const hashedPass = hashSync(password, salt)
      const newUser = {
        email,
        name,
        lastname,
        birthday: new Date(birthday),
        password: hashedPass,
        wallet: 100,
        userType: 'standard'
      }
      await usersRepository.insertUser(newUser)
      req.session.user = newUser.email
      req.session.wallet = newUser.wallet
      loggerW.info({
        type: "ALTA",
        method: req.method,
        url: req.originalUrl,
        params: req.params
      });
      res.redirect('/offers/my-offers?message=Cuenta creada con exito&messageType=alert-success')
    } else {
      req.session.user = null
      res.redirect(`/signup?message=${errors.join('<br>')}&messageType=alert-danger`)
    }

  })

  app.get('/login', (req, res) => {
    res.render('login.twig')
  })

  app.post('/login', async (req, res) => {
    const { email, password } = req.body

    const errors = []

    if (!email || !password) {
      errors.push('Es obligatorio rellenar todos los campos')
    }

    const user = await usersRepository.findUser({ email })

    if (!user || !compareSync(password, user.password)) {
      errors.push('Email o contraseña inválidos')
    }

    if (errors.length === 0) {
      req.session.user = user.email
      req.session.wallet = user.wallet
      req.session.isAdmin = user.userType === 'admin'
      loggerW.info({
        type: "LOGIN-EX",
        user: user.email
      });
      if(req.session.user === "admin@email.com"){
        res.redirect('/users?message=Sesión iniciada&messageType=alert-success')
      }else{
        res.redirect('/offers/my-offers?message=Sesión iniciada&messageType=alert-success')
      }
    } else {
      req.session.user = null
      loggerW.info({
        type: "LOGIN-ERR",
        user: email
      });
      res.redirect(`/login?message=${errors.join('<br>')}&messageType=alert-danger`)
    }

  })

  app.get('/logout', (req, res) => {
    loggerW.info({
      type: "LOGOUT",
      user: req.session.user
    });
    req.session.user = null
    res.redirect('/login')
  })

}