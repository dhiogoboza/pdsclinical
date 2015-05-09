package br.ufrn.pds.usersregister



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(["isAuthenticated()"])
@Transactional(readOnly = true)
class AFileController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

	def filesService
	
	def showImage(Long id) {
		println "no showImage"
		if(params.width && params.height) {
			filesService.showImage(id, request, response, params.width.toInteger(), params.height.toInteger())
		} else {
			filesService.showImage(id, request, response)
		}
    }
	
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AFile.list(params), model:[AFileInstanceCount: AFile.count()]
    }

    def show(AFile AFileInstance) {
        respond AFileInstance
    }

    def create() {
        respond new AFile(params)
    }

    @Transactional
    def save(AFile AFileInstance) {
        if (AFileInstance == null) {
            notFound()
            return
        }

        if (AFileInstance.hasErrors()) {
            respond AFileInstance.errors, view:'create'
            return
        }

        AFileInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'AFileInstance.label', default: 'AFile'), AFileInstance.id])
                redirect AFileInstance
            }
            '*' { respond AFileInstance, [status: CREATED] }
        }
    }

    def edit(AFile AFileInstance) {
        respond AFileInstance
    }

    @Transactional
    def update(AFile AFileInstance) {
        if (AFileInstance == null) {
            notFound()
            return
        }

        if (AFileInstance.hasErrors()) {
            respond AFileInstance.errors, view:'edit'
            return
        }

        AFileInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AFile.label', default: 'AFile'), AFileInstance.id])
                redirect AFileInstance
            }
            '*'{ respond AFileInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AFile AFileInstance) {

        if (AFileInstance == null) {
            notFound()
            return
        }

        AFileInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AFile.label', default: 'AFile'), AFileInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'AFileInstance.label', default: 'AFile'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
