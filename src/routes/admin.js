
/**
*
* @param {import("express").Application} app
* @param {import("../repositories/usersRepository")} usersRepository
*/
module.exports = function(app, usersRepository, ofersReposiroty, logsRepository) {
  app.get('/users', (req, res) => {
    const errors = []
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
        user: req.session.user,
        users: result.users,
        pages: pages,
        currentPage: page,
        admin: true
      }
      res.render("users.twig", response);
      
    }).catch(error => {
      errors.push('Se ha producido un error al recuperar los usuarios')
      res.redirect(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
    })
  })
  
  app.post('/users', (req,res) => {
    const errors = []
    if (typeof req.body.selectedUsers === 'string') {
      req.body.selectedUsers = [req.body.selectedUsers];
    }
    const selectedUsers = req.body.selectedUsers;
    if(selectedUsers != undefined && selectedUsers.length > 0){
      usersRepository.deleteUsers(selectedUsers).then (result => {
        ofersReposiroty.deleteOfferByAuthor(selectedUsers)
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
      errors.push('No hay usuarios seleccionados')
      res.redirect(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
    }
  })
  
  app.get('/logs', (req, res) => {
    const errors = []
    let filter = {};
    let options = {sort: { timestamp: -1}};
    
    let page = parseInt(req.query.page); // page is a string
    if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
      //In case there is no param
      page = 1;
    }
    logsRepository.getLogsPg(filter, options, page).then(result => {
      let lastPage = result.total / 10;
      if (result.total % 10 > 0) { // If there are decimals
        lastPage = lastPage + 1;
      }
      let pages = []; // pages to show
      for (let i = page - 2; i <= page + 2; i++) {
        if (i > 0 && i <= lastPage) {
          pages.push(i);
        }
      }
      let response = {
        user: req.session.user,
        logs: result.logs,
        pages: pages,
        currentPage: page,
        admin: true
      }
      res.render("logs.twig", response);
      
    }).catch(error => {
      errors.push('Se ha producido un error al recuperar los usuarios')
      res.send(`/users?message=${errors.join('<br>')}&messageType=alert-danger`)
    })
  })
  
  app.post('/logs', (req,res) => {
    const errors = []
    logsRepository.deleteLogs().then (result => {
      if (result === null || result.deletedCount === 0) {
        errors.push('No se han podido eliminar los logs')
        res.redirect(`/logs?message=${errors.join('<br>')}&messageType=alert-danger`)
      } else {
        res.redirect('/logs?message=Logs eliminados correctamente&messageType=alert-success')
      }
    }).catch(error => {
      errors.push('Se ha producido un error al intentar eliminar los logs')
      res.redirect(`/logs?message=${errors.join('<br>')}&messageType=alert-danger`)
    });
  })
  
}