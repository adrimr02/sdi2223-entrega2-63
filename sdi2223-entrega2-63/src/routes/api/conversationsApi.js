const {ObjectId} = require("mongodb");

module.exports = function (app, conversationRepository, messageRepository) {

    app.post('/api/conversations/:id/new', function (req, res) {
        console.log("nuevo mensajes")
        try{
            let message = {
                conversation:new ObjectId(req.body.converId),
                writer:req.user,
                content: req.body.content,
                date: new Date(),
                read: false

            };

            messageRepository.insertMessage(message).then(messages => {
                res.status(200);
                res.send(messages)
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

        let filter = {
            "conversation": new ObjectId(req.params.id)
        };
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

    app.post('/api/new/conversation/message', function (req, res) {
        console.log("Este es el mensaje que creeara la nueva conver: " +req.user)

        //Primero creamos la conversacion en la que meteremos el mensaje
        conversationRepository.insertConversation( {
            buyer: req.user,
            offer: new ObjectId(req.body.offer),
            offerTitle: req.body.offerTitle,
            date: new Date()
        }).then(newConverId => {
            //Ahora insertamos el mensaje con la id de la nueva conversacion
            console.log("Esto deberia de ser el id de la conver recien creada: "+ newConverId)
            messageRepository.insertMessage({
                    conversation:new ObjectId(newConverId),
                    content: req.body.content,
                    writer: req.user,
                    date: new Date(),
                    read: false
                }
            ).then(messages => {
                res.status(200);
                res.send({messages: messages})
            }).catch(error => {
                res.status(500);
                res.json({ error: "Se ha producido un error al insertar un nuevo mensaje." })
            });

        });


    })

    app.post('/api/new/conversation', function (req, res) {
        console.log("Estos son los datos que tienen que tienen que llegar " +req.body.id)
        let filter = {
                "offer": new ObjectId(req.body.id)
            };
        let options = {};
        conv = conversationRepository.findTheConversation(filter, options).then(conversations => {
            console.log(conversations)
            res.status(200);
            res.send({conversations: conversations})
        }).catch(error => {
            res.status(500);
            res.json({ error: "Se ha producido un error al recuperar las conversaciones." })
        });
    })


}