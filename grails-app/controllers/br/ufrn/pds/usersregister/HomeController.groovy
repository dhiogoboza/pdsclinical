package br.ufrn.pds.usersregister

import grails.plugin.springsecurity.annotation.Secured

@Secured(["isAuthenticated()"])
class HomeController {

    def index() {
		
		//render view: "/index"
		
		redirect controller: 'user', action: 'index'
		
	}
}
