const userSessionRouter = require('express').Router()

userSessionRouter.use(function (req, res, next) {
  if (req.session.user)
    if (req.session.isAdmin)
      next()
    else
      res.redirect('/offers/my-offers')
  else
    res.redirect('/login')
})

module.exports = userSessionRouter