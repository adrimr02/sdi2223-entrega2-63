const { hashSync, compareSync, genSaltSync } = require('bcrypt')

/**
 * 
 * @param {import("express").Application} app 
 * @param {import("../repositories/userRepository")} usersRepository 
 */
module.exports = function(app, usersRepository) {

  app.get('/signup', (req, res) => {
    res.render('signup.twig')
  })

  app.post('/signup', async (req, res) => {
    const { email, name, lastname, /*birthday,*/ password, repeatPassword } = req.body

    const errors = []

    if (!email || !name || !lastname /*|| birthday*/ || !password || !repeatPassword) {
      errors.push('Es obligatorio rellenar todos los campos')
    }

    const user = await usersRepository.findUser({ email })

    if (user) {
      errors.push('Ese email ya está en uso')
    }

    if (password !== repeatPassword) {
      errors.push('Las contraseñas no coinciden')
    }

    if (errors.length === 0) {
      const salt = genSaltSync(10)
      const hashedPass = hashSync(password, salt)
      const newUser = {
        email,
        name,
        lastname,
        //birthday,
        password: hashedPass,
        wallet: 100,
        userType: 'standard'
      }
      await usersRepository.insertUser(newUser)
      req.session.user = newUser.email
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
      res.redirect('/offers/my-offers?message=Sesión iniciada&messageType=alert-success')
    } else {
      req.session.user = null
      res.redirect(`/signup?message=${errors.join('<br>')}&messageType=alert-danger`)
    }

  })

  app.get('/logout', (req, res) => {
    req.session.user = null
    res.redirect('/login')
  })

}