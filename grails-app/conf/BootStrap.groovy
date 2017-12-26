import br.ufrn.pds.usersregister.*

class BootStrap {

	def addUser(name, username, password, email, roles, photo = null) {
        def user = new User(
            name: name,
            username: username,
            password: password,
            email: email,
			birthDate: new Date()
		).save(failOnError: true, flush: true)
        
        roles.each {
            UserRole.create user, it
        }
        
        return user
    }
	
	def addDefaultData() {
        def userRole = Role.findByAuthority(Roles.USER) ?: new Role(authority: Roles.USER).save(failOnError: true)
        def adminRole = Role.findByAuthority(Roles.ADMIN) ?: new Role(authority: Roles.ADMIN).save(failOnError: true)
		
		addUser("Admin", "admin", "admin", "admin@email.com", [adminRole]);
	}
	
    def init = { servletContext ->
		addDefaultData()
    }
	
    def destroy = {
		
    }
}
