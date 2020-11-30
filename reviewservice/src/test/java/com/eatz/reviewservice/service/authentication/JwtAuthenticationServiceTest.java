package com.eatz.reviewservice.service.authentication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;

import com.eatz.reviewservice.dto.LoginDTO;
import com.eatz.reviewservice.exception.CustomerNotFoundException;
import com.eatz.reviewservice.exception.UnauthorizedException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class JwtAuthenticationServiceTest {

	@InjectMocks
	JwtAuthenticationService jwtAuthenticationService;

	@Mock
	private Login login;

	@Mock
	PasswordEncoder passwordEncoder;

	String email = "sr@email.com";
	String password = "password";
	String firstName = "firstName";
	String lastName = "lastName";
	long id = 1L;
	String url = "http://localhost:8000/customer/customerByEmail";

	@BeforeEach
	public void setup() {
		org.springframework.test.util.ReflectionTestUtils.setField(jwtAuthenticationService, "customerServiceEmail",
				url);
	}

	@Test
	public void authenticateUser_invalidEmail() throws UnauthorizedException {
		LoginDTO user = new LoginDTO();
		user.setEmail(email);
		user.setPassword(password);
		when(login.loginCustomer(email)).thenThrow(HttpClientErrorException.class);
		Assertions.assertThrows(CustomerNotFoundException.class, () -> {
			jwtAuthenticationService.authenticateUser(user);
		});

	}

	@Test
	public void authenticateUser_invalidpassword() {
		LoginDTO user = new LoginDTO();
		user.setEmail(email);
		user.setPassword(password);
		when(login.loginCustomer(email)).thenReturn(user);
		when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);
		Assertions.assertThrows(UnauthorizedException.class, () -> {
			jwtAuthenticationService.authenticateUser(user);
		});

	}

	@Test
	public void authenticateUser() throws UnauthorizedException {
		LoginDTO user = new LoginDTO(email, password);
		when(login.loginCustomer(email)).thenReturn(user);
		when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
		String jwt = jwtAuthenticationService.authenticateUser(user);
		assertNotNull(jwt);
	}

}
