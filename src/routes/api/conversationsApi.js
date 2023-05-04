
module.exports = function (app, conversationRepository) {
    app.get('/api/users/conversation', async (req, res) => {
        let filter = {};
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