const userSessionRouter = require('express').Router()

userSessionRouter.use(function (req, res, next) {
    if (!req.session.user)
        res.redirect('/login?message=No tiene permitido el acceso a esa parte de la web.&messageType=alert-danger')
    else
        next()
})

module.exports = userSessionRouter