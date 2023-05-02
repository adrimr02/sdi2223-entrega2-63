const { ObjectId } = require("mongodb")

/**
 * 
 * @param {import("express").Application} app 
 * @param {import("../repositories/offersRepository")} offerRepo
 * @param {import("../repositories/usersRepository")} userRepo
 */
module.exports = function(app, offerRepo, userRepo) {

  app.get('/shop', async (req, res) => {
    console.log(req.query)
    let filter = {}
    let options = {sort: { title: 1}}
    
    if(req.query.search){
      filter.title = { $regex: `.*${req.query.search}.*` }
    }
    const page = req.query.page || 1
    const { offers, total: totalOfferCount } = await offerRepo.getOffersPage(filter, options, page, 5)
    const featured = await offerRepo.getOffers({ featured: true }, {})
    const pages = getPages(totalOfferCount, page, 5)
    res.render("shop.twig", {
      user: req.session.user,
      offers: offers.map(o => ({ ...o, date: formatDate(o.date) })),
      featured: featured.map(o => ({ ...o, date: formatDate(o.date) })),
      pages,
      currentPage: page+'',
      search: req.query.search
    })

  })

  app.get('/offers/new', (req, res) => {
    res.render('offers/new')
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
      seller: req.session.user,
      available: true,
      featured: req.body.featured || false
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
      if (offer.featured)
        await userRepo.updateUser({ email: req.session.user }, { $set: {wallet: user.wallet - 20 } })
      await offerRepo.insertOffer(offer)
      res.redirect('/offers/my-offers?message=Oferta creada.&messageType=alert-success')
    } else {
      res.redirect(`/offers/new?message=${errors.join('<br>')}&messageType=alert-danger`)
    }

  })

  app.get('/offers/my-offers', async (req, res) => {

    const filter = { author: req.session.user }
    const options = { sort: { date: -1 } }

    const page = req.query.page || 1
    const { offers, total: totalOfferCount } = await offerRepo.getOffersPage(filter, options, page )

    const pages = getPages(totalOfferCount, page)


    res.render('offers/myOffers', {
      user: req.session.user,
      offers: offers.map(o => ({ ...o, date: formatDate(o.date) })),
      currentPage: page,
      pages
    })
  })

  app.get('/offers/bought', async (req, res) => {

    const filter = { available:false, buyer: req.session.user }
    const options = { sort: { date: -1 } }

    const page = req.query.page || 1
    const { offers, total: totalOfferCount } = await offerRepo.getOffersPage(filter, options, page)

    const pages = getPages(totalOfferCount, page)

    res.render('offers/bought', {
      user: req.session.user,
      offers,
      currentPage: page,
      pages
    })
  })

  app.get('/offers/delete/:id', async (req, res) => {
    try {
      const offer = await offerRepo.findOffer({ _id: new ObjectId(req.params.id) }, {})
      if (!offer) {
        res.redirect('/offers/my-offers?message=La oferta que intentas eliminar no existe.&messageType=alert-danger')
        return
      }
      if (offer.author !== req.session.user) {
        res.redirect('/offers/my-offers?message=Esa oferta no es tuya. No puedes eliminarla.&messageType=alert-danger')
        return
      }

      const result = await offerRepo.deleteOffer(req.params.id)
      if (result === null || result.deletedCount === 0) {
        res.redirect('/offers/my-offers?message=No se ha podido eliminar la oferta.&messageType=alert-danger')
      } else {
        res.redirect('/offers/my-offers?message=Oferta eliminada.&messageType=alert-success')
      }
    } catch(err) {
      console.log(err)
      res.redirect('/offers/my-offers?message=Se ha producido un error al eliminar la oferta.&messageType=alert-danger')
    }
  })

  app.get('/offers/buy/:id', async (req, res) => {
    try {
      const offer = await offerRepo.findOffer({ _id: new ObjectId(req.params.id) }, {})
      if (!offer) {
        res.redirect('/shop?message=La oferta que intentas comprar no existe.&messageType=alert-danger')
        return
      }
      if (offer.author === req.session.user) {
        res.redirect('/shop?message=No puedes comprar tu propia oferta.&messageType=alert-danger')
        return
      }
      if (!offer.available) {
        res.redirect('/shop?message=No puedes comprar tu propia oferta.&messageType=alert-danger')
        return
      }
      const user = await userRepo.findUser({ email: req.session.user }, {})
      if (user.wallet < offer.price) {
        if (!offer) {
          res.redirect('/shop?message=No tienes suficiente dinero.&messageType=alert-danger')
          return
        }
      }
      await userRepo.updateUser({ _id: user._id }, { $set: {wallet: user.wallet - offer.price } })
      await offerRepo.updateOffer(new ObjectId(req.params.id), { $set: { available: false, buyer: req.session.user }})
      res.redirect('/offers/bought?message=Oferta comprada.&messageType=alert-success')
    } catch(err) {
      console.log(err)
      res.redirect('/shop?message=Se ha producido un error al comprar la oferta.&messageType=alert-danger')
    }
  })

}

function getPages(total, currentPage, pageSize = 8) {
  const pages = []

  let lastPage = total / pageSize
  if (total % pageSize > 0)
    lastPage += 1

  for (let i = currentPage - 2; i <= currentPage + 2; i++)
    if (i > 0 && i <= lastPage)
      pages.push(i)

  return pages
}

/**
 * 
 * @param {Date} date 
 * @returns date in format DD/MM/YYYY
 */
function formatDate(date) {
  if (!(date instanceof Date)) {
    throw new Error('Invalid "date" argument. You must pass a date instance')
  }

  const day = String(date.getDate()).padStart(2, '0')
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const year = date.getFullYear()

  return `${day}/${month}/${year}`
}