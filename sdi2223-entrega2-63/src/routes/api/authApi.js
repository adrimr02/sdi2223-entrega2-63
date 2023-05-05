const { compareSync } = require("bcrypt")


/**
 * 
 * @param {import("express").Application} app 
 * @param {import ('../../repositories/usersRepository')} usersRepo 
 */
module.exports = function (app, usersRepo) {
  app.post('/api/users/login', async (req, res) => {
    const { email, password } = req.body
    if (!email || !password) {
      res.status(400).json({ success: false, error: { message: 'Falta email o contraseña' } })
      return
    }
    try {
      const user = await usersRepo.findUser({ email })
      if (!user) {
        res.status(400).json({ success: false, error: { message: 'Email o contraseña incorrectos' } })
        return
      }
  
      if (!compareSync(password, user.password)) {
        res.status(400).json({ success: false, error: { message: 'Email o contraseña incorrectos' } })
        return
      }

      const token = app.get('jwt').sign({ user: user.email, time: Date.now()/1000 }, app.get('clave'))
      res.status(200).json({ success: true, data: { token, message: 'Usuario autorizado', autheticated: true, email } })
    } catch(err) {
      res.status(500).json({ success: false, error: { message: 'Ha ocurrido un error al iniciar sesión' } })
    }
  })
}