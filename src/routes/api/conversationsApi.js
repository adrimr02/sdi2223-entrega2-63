
module.exports = function (app, conversationRepository, messageRepository) {


    app.post('/api/conversations/:id/new', function (req, res) {
        console.log("nuevo mensajes")
        try{
            let message = {
                buyer: "user01@email.com",
                offer: "Sofa",
                seller:"user02@email.com",
                writer:"user01@email.com",
                content: req.body.content,
                date: new Date(),
                read: false
            };

            messageRepository.insertMessage(message).then(messages => {
                res.status(200);
            }).catch(error => {
                res.status(500);
                res.json({ error: "Se ha producido un error al insertar un nuevo mensaje." })
            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error al intentar crear un mensaje: " + e})
        }
    });


    app.get('/api/conversations/:id', function (req, res) {

        console.log("Llegue mensajes")

        let filter = {"$and":[
                {"buyer": "user01@email.com"},
                {"seller": "user02@email.com"},
                {"offer": "Sofa"}
            ]};
        let options = {};
        messageRepository.findMessages(filter, options).then(messages => {
            res.status(200);
            res.send({messages: messages})
        }).catch(error => {
            res.status(500);
            res.json({ error: "Se ha producido un error al recuperar las conversaciones." })
        });
    })



    app.post('/api/conversations', function (req, res) {
        console.log("Llegue")
        console.log(req.user  +" este")
        let filter = {"$or":[
                {"buyer": req.user},
                {"seller": req.user}
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