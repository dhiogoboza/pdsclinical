package br.ufrn.pds.usersregister



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import grails.plugin.springsecurity.annotation.Secured


@Secured(["isAuthenticated()"])
class UserController {
	
	def filesService
	def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

	@Secured(["isAuthenticated()"])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userInstanceCount: User.count()]
    }

	@Secured(["isAuthenticated()"])
    def show(User userInstance) {
        respond userInstance
    }

	@Secured(["permitAll()"])
    def create() {
        respond new User(params)
    }

	@Secured(["permitAll()"])
    def save(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view:'create'
            return
        }
		
		
		def path = "photos"
        def f
		
        try {
            f = request.getFile('photo_')
            //println "logo:"+f.originalFilename
            if(f && f.originalFilename != "") {
				def user = User.findByUsername("admin")
				
                userInstance.photo = filesService.saveFile("images", f, user, path)
            }
        } catch (Exception e){
			e.printStackTrace()
			
            flash.message = e
            render(view: "create", model: [userInstance: userInstance])
            return
        }
		

        userInstance.save(flush:true)

        /*request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userInstance.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: CREATED] }
        }*/
		flash.message = message(code: 'default.created.message', args: [message(code: 'userInstance.label', default: 'User'), userInstance.id])
		
		redirect action: "index"
    }

	@Secured(["isAuthenticated()"])
    def edit(User userInstance) {
        respond userInstance
    }

    @Secured(["isAuthenticated()"])
    def update(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view:'edit'
            return
        }

		def path = "photos"
        def f
		
        try {
            f = request.getFile('photo_')
            //println "logo:"+f.originalFilename
            if(f && f.originalFilename != "") {
                userInstance.photo = filesService.saveFile("images", f, springSecurityService.currentUser, path)
            }
        } catch (Exception e){
			e.printStackTrace()
			
            flash.message = e
            render(view: "create", model: [userInstance: userInstance])
            return
        }
		
        userInstance.save flush:true

        /*request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*'{ respond userInstance, [status: OK] }
        }*/
		
		flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
		
		redirect action: "index"
    }

    @Secured(["isAuthenticated()"])
    def delete(User userInstance) {

        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userInstance.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
