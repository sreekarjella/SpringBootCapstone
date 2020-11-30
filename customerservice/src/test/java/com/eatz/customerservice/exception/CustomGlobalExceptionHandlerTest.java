package com.eatz.customerservice.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CustomGlobalExceptionHandlerTest {

	@Autowired
	CustomGlobalExceptionHandler handler;

	@Test
	void testExceptionUnauthorizedException() {
		UnauthorizedException exception = new UnauthorizedException("exception message");
		ResponseEntity<Object> response = handler.exception(exception);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	void testExceptionInvalidTokenException() {
		InvalidTokenException exception = new InvalidTokenException("exception message");
		ResponseEntity<Object> response = handler.exception(exception);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	void testExceptionCustomerException() {
		CustomerException exception = new CustomerException("exception message");
		ResponseEntity<Object> response = handler.exception(exception);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}

	@Test
	void testExceptionCustomerNotFoundException() {
		CustomerNotFoundException exception = new CustomerNotFoundException("exception message");
		ResponseEntity<Object> response = handler.exception(exception);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	void testExceptionException() {
		Exception exception = new Exception("exception message");
		ResponseEntity<Object> response = handler.exception(exception);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
