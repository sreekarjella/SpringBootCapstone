package com.eatz.customerservice.service.authentication;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.exception.CustomerNotFoundException;
import com.eatz.customerservice.exception.UnauthorizedException;
import com.eatz.customerservice.model.Customer;
import com.eatz.customerservice.repository.CustomerRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationService.class);

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final long EXPIRATION_TIME = 900000;

	public String authenticateUser(LoginDTO user) throws UnauthorizedException {
		Optional<Customer> customer = repository.findCustomerByEmail(user.getEmail());
		if (!customer.isPresent()) {
			logger.debug("Email is invalid");
			throw new CustomerNotFoundException("Please check your email or register for a new account");
		}
		if (!passwordEncoder.matches(user.getPassword(), customer.get().getPassword())) {
			logger.debug("Password is invalid");
			throw new UnauthorizedException("Invalid Credentials");

		}
		logger.debug("Authentication is successfull. Generating token");
		return Jwts.builder().setSubject(user.getEmail()).claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey")
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).compact();

	}

}
