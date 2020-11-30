package com.eatz.reviewservice.service.authentication;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.eatz.reviewservice.dto.LoginDTO;
import com.eatz.reviewservice.exception.CustomerNotFoundException;
import com.eatz.reviewservice.exception.UnauthorizedException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationService.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Login login;

	private static final long EXPIRATION_TIME = 900000;

	public String authenticateUser(LoginDTO user) throws UnauthorizedException {
		logger.debug("Checking for user login details");
		validateLogin(user);
		logger.debug("Authentication is successfull. Generating token");
		return Jwts.builder().setSubject(user.getEmail()).claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey")
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).compact();

	}

	private void validateLogin(LoginDTO user) throws UnauthorizedException {
		LoginDTO loginDetails;
		try {
			logger.debug("Calling the customer service");
			loginDetails = login.loginCustomer(user.getEmail());
		} catch (HttpClientErrorException e) {
			logger.debug("Email is invalid");
			throw new CustomerNotFoundException("Please check your email or register for a new account");
		}
		if (!passwordEncoder.matches(user.getPassword(), loginDetails.getPassword())) {
			logger.debug("Password is invalid");
			throw new UnauthorizedException("Invalid Credentials");
		}
	}

}
