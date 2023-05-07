const {ObjectId} = require("mongodb");

module.exports = function (app, conversationRepository, messageRepository) {

    app.post('/api/conversations/:id/new', function (req, res) {
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
        if (req.body.content.toString().trim() === '') {
            res.status(400).json({ success: false, error: { message: 'Mensaje en blanco' } })
            return
        }

        if(req.user === req.body.offerSeller){
            res.status(400).json({ success: false, error: { message: 'Mensaje a su propia oferta' } })
            return
        }


        //Primero creamos la conversacion en la que meteremos el mensaje
        conversationRepository.insertConversation( {
            buyer: req.user,
            offer: new ObjectId(req.body.offer),
            seller: req.body.offerSeller,
            offerTitle: req.body.offerTitle,
            date: new Date()
        }).then(newConverId => {

            //Ahora insertamos el mensaje con la id de la nueva conversacion
           let newMessage = {
                conversation:new ObjectId(newConverId),
                content: req.body.content,
                writer: req.user,
                date: new Date(),
                read: false
            }
            messageRepository.insertMessage(newMessage).then(messages => {
                res.status(200);
                res.send({messages: newMessage})
            }).catch(error => {
                res.status(500);
                res.json({ error: "Se ha producido un error al insertar un nuevo mensaje." })
            });

        });


    })

    app.post('/api/new/conversation', function (req, res) {
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