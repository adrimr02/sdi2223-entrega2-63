const userSessionRouter = require('express').Router()

userSessionRouter.use(function (req, res, next) {
    if (req.session.user)
      res.redirect('/offers/my-offers')
    else
      next()
})

module.exports = userSessionRouter