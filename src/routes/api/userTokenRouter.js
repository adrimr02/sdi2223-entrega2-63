const router = require('express').Router()

router.use((req, res, next) => {
  let token = req.headers['token'] || req.body.token || req.query.token
  if (token != null) {
    // verificar el token
    jwt.verify(token, 'secreto', {}, function (err, infoToken) {
      if (err || (Date.now() / 1000 - infoToken.time) > 240) {
        res.status(403).json({
          success: false,
          error: 'Token inválido o caducado',
          authorized: false,
        })
      } else {
        // dejamos correr la petición
        req.user = infoToken.user
        next()
      }
    })
  } else {
    res.status(403) // Forbidden
    res.json({
      authorized: false,
      error: 'No hay Token'
    })
  }
})