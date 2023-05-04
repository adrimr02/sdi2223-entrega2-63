
module.exports = function (app, conversationRepository) {
    app.post('/api/conversations', function (req, res) {
        const {email}= req.body
        console.log("Llegue")
        console.log(email +"este")
        let filter = {"$or":[
                {"buyer": "user01@email.com"},
                {"seller": "user01@email.com"}
            ]};
        let options = {};
        conversationRepository.findConversation(filter, options).then(conversations => {
            res.status(200);
            res.send({conversations: conversations})
        }).catch(error => {
            res.status(500);
            res.json({ error: "Se ha producido un error al recuperar las conversaciones." })
        });
    })
}