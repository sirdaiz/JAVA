package com.mycompany.analytics

import org.springframework.ldap.core.DirContextAdapter
import org.springframework.ldap.core.DirContextOperations
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper
import org.springframework.security.core.authority.SimpleGrantedAuthority

class MyUserDetailsContextMapper implements UserDetailsContextMapper {

   UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                  Collection authorities) {
//	  println "vamos bien"+ctx.originalAttrs
	  println "vamos bien 2"+ctx.originalAttrs.values
      String fullname = ctx.originalAttrs['name'].values[0]
      String email = ctx.originalAttrs['mail'].values[0].toString().toLowerCase()
      def title = "titulo"//ctx.originalAttrs['memberOf']

	  ctx.originalAttrs['memberOf'].values.each { authe ->
		  def result = authe.split(",")
		  if (result && result.size()==4) {		
			authorities.add(new SimpleGrantedAuthority("ROLE_" + result[0].split("=")[1].toUpperCase()))
		  }
	  }
	  println "authorities: " + authorities
      new MyUserDetails(username, "pass", true, true, true, true, authorities, fullname, email, title)
   }

   void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
      throw new IllegalStateException("Only retrieving data from AD is currently supported")
   }
}