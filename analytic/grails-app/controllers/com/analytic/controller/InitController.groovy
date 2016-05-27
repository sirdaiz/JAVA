
package com.analytic.controller
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class InitController {
	private final Logger logger = LoggerFactory.getLogger(InitController.class);
	
	@Secured(['ROLE_USERCRMWEB'])
    def index() {
		logger.info "Hola info"
		logger.error "Hola error"
		
		[saludo:"HolaPepe"]	
	}
}
