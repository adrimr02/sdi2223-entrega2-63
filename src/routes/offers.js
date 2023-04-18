/**
 * 
 * @param {import("express").Application} app 
 * @param {import("../repositories/offersRepository")} offerRepo 
 */
module.exports = function(app, offerRepo) {

  app.get('/offers/new', (req, res) => {
    res.render('offers/new.twig')
  })

  app.post('/offers/new', async (req, res) => {
    if (!req.body.title.trim() || !req.body.price.trim()) {
      res.redirect('/offers/new?message=Debes incluir, al menos, el título y el precio&messageType=alert-danger')
      return
    }

    const offer = {
      title: req.body.title.trim(),
      description: (req.body.description || '').trim(),
      price: parseFloat(req.body.price),
      date: new Date(),
      author: req.session.user
    }

    const errors = []

    if (offer.price <= 0) {
      errors.push('El precio debe ser mayor que 0')
    }

    if (offer.title.length < 5) {
      errors.push('El título debe tener al menos 5 caracteres')
    }

    if (offer.title.length > 30) {
      errors.push('El título debe tener menos de 30 caracteres')
    }

    if (errors.length === 0) {
      await offerRepo.insertOffer(offer)
      res.redirect('offers/my-offers')
    } else {
      res.redirect(`/offers/new?message=${errors.join('<br>')}&messageType=alert-danger`)
    }

  })

}