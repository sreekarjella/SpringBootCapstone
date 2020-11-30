package com.eatz.customerservice.service.authentication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.exception.CustomerNotFoundException;
import com.eatz.customerservice.exception.UnauthorizedException;
import com.eatz.customerservice.model.Customer;
import com.eatz.customerservice.repository.CustomerRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class JwtAuthenticationServiceTest {

	@InjectMocks
	JwtAuthenticationService jwtAuthenticationService;

	@Mock
	CustomerRepository repository;

	@Mock
	PasswordEncoder passwordEncoder;

	String email = "email@test.com";
	String password = "password";
	String firstName = "firstName";
	String lastName = "lastName";
	long id = 1L;

	@Test
	public void authenticateUser_invalidEmail() throws UnauthorizedException {
		LoginDTO user = new LoginDTO();
		user.setEmail(email);
		user.setPassword(password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.empty());
		Assertions.assertThrows(CustomerNotFoundException.class, () -> {
			jwtAuthenticationService.authenticateUser(user);
		});

	}

	@Test
	public void authenticateUser_invalidpassword() {
		LoginDTO user = new LoginDTO();
		user.setEmail(email);
		user.setPassword(password);
		Customer customer = new Customer(id, firstName, lastName, email, password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.of(customer));
		when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);
		Assertions.assertThrows(UnauthorizedException.class, () -> {
			jwtAuthenticationService.authenticateUser(user);
		});

	}

	@Test
	public void authenticateUser() throws UnauthorizedException {
		LoginDTO user = new LoginDTO(email, password);
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setFirstname(firstName);
		customer.setLastname(lastName);
		customer.setId(id);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.of(customer));
		when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
		String jwt = jwtAuthenticationService.authenticateUser(user);
		assertNotNull(jwt);
	}

}
