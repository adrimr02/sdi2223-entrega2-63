
module.exports = function (app, offersRepository) {
    app.get("/api/offers", function (req, res) {
        let filter = {seller: {$ne: req.user},
                    available: true};
        let options = {};
        offersRepository.getOffers(filter, options).then(offers => {
            res.status(200);
            res.send({offers: offers})
        }).catch(error => {
            res.status(500);
            res.json({ error: "Se ha producido un error al recuperar las ofertas." })
        });
    });
}
