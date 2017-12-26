package br.ufrn.pds.usersregister

class User {
	
	transient springSecurityService
	
	String name
	String username
    String password
	String email
	String cellPhone
	String phone
	Date birthDate
	AFile photo
	
	String street
	String complement
	String district
	String zip
	String city
	String state
	Integer number
	
    
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    static constraints = {
		password blank: false, password: true
        username blank: false, unique: true
		email blank: false, unique: true
		
		cellPhone nullable: true
		phone nullable: true
		birthDate nullable: true
		photo nullable: true
		
		street nullable: true
		complement nullable: true
		district nullable: true
		zip nullable: true
		city nullable: true
		state nullable: true
		number nullable: true
    }
	
	static mapping = {
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }
        
    def beforeValidate() {
        
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
        
    static List<User> getUsersByRole(long roleId) {
        println 'select distinct u from UserRole ur inner join ur.user u where ur.role.id='+roleId
        
        executeQuery 'select distinct u from UserRole ur inner join ur.user u where ur.role.id=:roleId',
        [roleId: roleId]
    }
    
    String toString() {
        return name + " [" + email + "]"
    }
    
    public static User getOlder() {
        return User.find("from User order by registeredAt")
    }
}
