const {ObjectId} = require("mongodb");

/**
 *
 * @param {import("express").Application} app
 * @param {import("../repositories/usersRepository")} usersRepository
 */
module.exports = function(app, usersRepository) {
    app.get('/users', (req, res) => {
        let filter = {};
        let options = {sort: { email: 1}};

        let page = parseInt(req.query.page); // page is a string
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            //In case there is no param
            page = 1;
        }
        usersRepository.getUsersPg(filter, options, page).then(result => {
            let lastPage = result.total / 5;
            if (result.total % 5 > 0) { // If there are decimals
                lastPage = lastPage + 1;
            }
            let pages = []; // pages to show
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage) {
                    pages.push(i);
                }
            }
            let response = {
                users: result.users,
                pages: pages,
                currentPage: page
            }
            res.render("users.twig", response);

        }).catch(error => {
            res.send("Se ha producido un error al listar los usuarios " + error)
        })
    })

    app.post('/users', (req,res) => {
        const errors = []
        const selectedUsers = req.body.selectedUsers;
        if(selectedUsers != undefined && selectedUsers.length > 0){
            if(!selectedUsers.include("admin@email.com")){
                usersRepository.deleteUsers(selectedUsers).then (result => {
                    if (result === null || result.deletedCount === 0) {
                        errors.push('No se han podido eliminar los usuarios')
                        res.redirect(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
                    } else {
                        res.redirect('/users?message=Usuarios eliminados correctamente&messageType=alert-success')
                    }
                }).catch(error => {
                    errors.push('Se ha producido un error al intentar eliminar los usuarios')
                    res.redirect(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
                });
            }else{
                errors.push('No puede eliminar al usuario administrador')
                res.redirect(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
            }
        }else{
            errors.push('No hay usuarios seleccionados')
            res.redirect(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
        }
    })
}