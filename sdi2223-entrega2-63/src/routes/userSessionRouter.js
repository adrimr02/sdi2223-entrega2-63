const userSessionRouter = require('express').Router()

userSessionRouter.use(function (req, res, next) {
  if (req.session.user)
    if (!req.session.isAdmin) {
      next()
    }
    else
      res.redirect('/users?message=No tiene permitido el acceso a esa parte de la web&messageType=alert-danger')
  else
    res.redirect('/login')
})

module.exports = userSessionRouter