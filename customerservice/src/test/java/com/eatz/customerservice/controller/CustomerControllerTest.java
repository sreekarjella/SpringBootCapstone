package com.eatz.customerservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.eatz.customerservice.CustomerserviceApplication;
import com.eatz.customerservice.dto.CustomerDTO;
import com.eatz.customerservice.dto.CustomerResponseDTO;
import com.eatz.customerservice.dto.CustomerUpdateDTO;
import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.model.Customer;
import com.eatz.customerservice.service.customer.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CustomerserviceApplication.class)
@WebMvcTest(value = CustomerController.class)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMVC;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerService customerService;

	private static final long EXPIRATIONTIME = 900000;
	String jwt = "";
	long id = 1L;
	String firstName = "firstName";
	String lastName = "lastName";
	String email = "test@email.com";
	String password = "password";

	@BeforeEach
	public void setup() {
		jwt = "Bearer " + Jwts.builder().setSubject("user").claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey")
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)).compact();
	}

	@Test
	void registerCustomerTest() throws Exception {
		CustomerDTO customer = new CustomerDTO(firstName, lastName, email, password);
		Customer response = new Customer(id, firstName, lastName, email, password);
		when(customerService.registerCustomer(any(CustomerDTO.class))).thenReturn(response);
		RequestBuilder request = MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer));
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

	@Test
	void updateCustomerTest() throws Exception {
		CustomerUpdateDTO update = new CustomerUpdateDTO(id, firstName, lastName, email, password);
		Customer response = new Customer(id, firstName, lastName, email, password);
		when(customerService.updateCustomer(any(CustomerUpdateDTO.class))).thenReturn(response);
		RequestBuilder request = MockMvcRequestBuilders.put("/customerDetails").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(update)).header(HttpHeaders.AUTHORIZATION, jwt);
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

	@Test
	void getCustomerByIDTest() throws Exception {
		CustomerResponseDTO response = new CustomerResponseDTO(id, firstName, lastName, email);
		when(customerService.getCustomerById(any(Long.class))).thenReturn(response);
		RequestBuilder request = MockMvcRequestBuilders.get("/customerById").accept(MediaType.ALL)
				.param("id", String.valueOf(id)).header(HttpHeaders.AUTHORIZATION, jwt);
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

	@Test
	void getCustomerByEmailTest() throws Exception {
		LoginDTO response = new LoginDTO(email, password);
		when(customerService.getCustomerByEmail(any(String.class))).thenReturn(response);
		RequestBuilder request = MockMvcRequestBuilders.get("/customerByEmail").accept(MediaType.ALL)
				.param("email", email);
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

}
