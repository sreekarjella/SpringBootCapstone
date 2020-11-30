package com.eatz.customerservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.exception.UnauthorizedException;
import com.eatz.customerservice.service.authentication.JwtAuthenticationService;


@RestController
public class JwtAuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	JwtAuthenticationService authenticationService;


	@PostMapping("/login")
	public ResponseEntity<String> enroll(@RequestBody LoginDTO user) throws UnauthorizedException  {
		logger.debug("Calling authentication service to verify user");
		String token = authenticationService.authenticateUser(user);
		logger.debug("User verified, returning back token");	 
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(token);


	}

}
