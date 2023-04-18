/**
 * 
 * @param {import("express").Application} app 
 * @param {import("../repositories/offersRepository")} offerRepo 
 */
module.exports = function(app, offerRepo) {

  app.get('/offers/new', (req, res) => {
    


  })

  app.post('/offers/new', (req, res) => {
    if (!req.body.title.trim() || !req.body.price.trim()) {
      res.redirect('/offers/new?message=Debes incluir un título y un precio&messageType=alert-danger')
      return
    }

    const offer = {
      title: req.body.title.trim(),
      description: req.body.trim() || '',
      price: parseFloat(req.body.price)
    }

    

  })

}