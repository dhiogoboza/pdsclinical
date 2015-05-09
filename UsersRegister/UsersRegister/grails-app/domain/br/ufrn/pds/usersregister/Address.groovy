
package br.ufrn.pds.usersregister

/**
 *
 * 
 */

class Address {
	
	String street
	String complement
	String district
	String zip
	String city
	String province
	Integer number
	
	static constraints = {
		
		street nullable: true
		complement nullable: true
		district nullable: true
		zip nullable: true
		city nullable: true
		province nullable: true
		number nullable: true
		
    }
	
}

