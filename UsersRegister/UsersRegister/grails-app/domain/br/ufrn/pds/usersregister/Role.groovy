package br.ufrn.pds.usersregister

/**
 *
 * @version 1.0
 * @since 1.0
 */
class Role {

    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }
	
    String toString() {
        return authority
    }
}
