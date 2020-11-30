package com.eatz.customerservice.service.customer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eatz.customerservice.dto.CustomerDTO;
import com.eatz.customerservice.dto.CustomerUpdateDTO;
import com.eatz.customerservice.exception.CustomerException;
import com.eatz.customerservice.exception.CustomerNotFoundException;
import com.eatz.customerservice.model.Customer;
import com.eatz.customerservice.repository.CustomerRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CustomerServiceImplTest {

	@InjectMocks
	private CustomerServiceImpl service;

	@Mock
	private CustomerRepository repository;

	@Mock
	private PasswordEncoder passwordEncoder;

	long id = 1L;
	String firstName = "firstName";
	String lastName = "lastName";
	String email = "email@test.com";
	String password = "password";

	@Test
	void testRegisterCustomer() {
		CustomerDTO customerDTO = new CustomerDTO(firstName, lastName, email, password);
		Customer customer = new Customer(id, firstName, lastName, email, password);
		when(repository.save(any(Customer.class))).thenReturn(customer);
		Customer response = service.registerCustomer(customerDTO);
		assertNotNull(response);
	}

	@Test
	void testRegisterCustomer_duplicateRegistration() {
		CustomerDTO customerDTO = new CustomerDTO(firstName, lastName, email, password);
		Customer customer = new Customer(id, firstName, lastName, email, password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.of(customer));
		assertThrows(CustomerException.class, () -> {
			service.registerCustomer(customerDTO);
		});
	}

	@Test
	void testRegisterCustomer_invalidFirstName() {
		CustomerDTO customerDTO = new CustomerDTO("123", lastName, email, password);
		assertThrows(CustomerException.class, () -> {
			service.registerCustomer(customerDTO);
		});
	}

	@Test
	void testRegisterCustomer_invalidLastName() {
		CustomerDTO customerDTO = new CustomerDTO(firstName, "123", email, password);
		assertThrows(CustomerException.class, () -> {
			service.registerCustomer(customerDTO);
		});
	}

	@Test
	void testRegisterCustomer_invalidEmail() {
		CustomerDTO customerDTO = new CustomerDTO(firstName, lastName, "testmailcom", password);
		assertThrows(CustomerException.class, () -> {
			service.registerCustomer(customerDTO);
		});
	}

	@Test
	void testRegisterCustomer_invalidPassword() {
		CustomerDTO customerDTO = new CustomerDTO(firstName, lastName, email, "");
		assertThrows(CustomerException.class, () -> {
			service.registerCustomer(customerDTO);
		});
	}

	@Test
	void testUpdateCustomer() {
		CustomerUpdateDTO updateRequest = new CustomerUpdateDTO(id, firstName, lastName, email, password);
		Customer customer = new Customer(id, firstName, lastName, email, password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.of(customer));
		when(repository.save(any(Customer.class))).thenReturn(customer);
		assertNotNull(service.updateCustomer(updateRequest));
	}

	@Test
	void testUpdateCustomer_duplicateUpdate() {
		CustomerUpdateDTO updateRequest = new CustomerUpdateDTO(id, firstName, lastName, email, password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.empty());
		assertThrows(CustomerException.class, () -> {
			service.updateCustomer(updateRequest);
		});
	}

	@Test
	void testUpdateCustomer_badID() {
		CustomerUpdateDTO updateRequest = new CustomerUpdateDTO(id, firstName, lastName, email, password);
		Customer customer = new Customer(2L, firstName, lastName, email, password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.of(customer));
		assertThrows(CustomerException.class, () -> {
			service.updateCustomer(updateRequest);
		});
	}

	@Test
	void testGetCustomerById() {
		Customer customer = new Customer(id, firstName, lastName, email, password);
		when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));
		assertNotNull(service.getCustomerById(id));
	}

	@Test
	void testGetCustomerById_badID() {
		when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
		assertThrows(CustomerNotFoundException.class, () -> {
			service.getCustomerById(id);
		});
	}

	@Test
	void testGetCustomerByEmail() {
		Customer customer = new Customer(id, firstName, lastName, email, password);
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.of(customer));
		assertNotNull(service.getCustomerByEmail(email));
	}

	@Test
	void testGetCustomerByEmail_badEmail() {
		when(repository.findCustomerByEmail(any(String.class))).thenReturn(Optional.empty());
		assertThrows(CustomerNotFoundException.class, () -> {
			service.getCustomerByEmail(email);
		});
	}

}
