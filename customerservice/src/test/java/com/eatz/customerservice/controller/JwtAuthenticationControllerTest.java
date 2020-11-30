package com.eatz.customerservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.eatz.customerservice.CustomerserviceApplication;
import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.service.authentication.JwtAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = JwtAuthenticationController.class)
@ContextConfiguration(classes = CustomerserviceApplication.class)
class JwtAuthenticationControllerTest {

	@Autowired
	private MockMvc mockMVC;

	@MockBean
	JwtAuthenticationService authenticationService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void authenticate() throws Exception {
		LoginDTO login = new LoginDTO();
		login.setEmail("sr@email.com");
		login.setPassword("password");
		String token = "jwtToken";
		when(authenticationService.authenticateUser(any(LoginDTO.class))).thenReturn(token);

		RequestBuilder request = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login));
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();

	}

}
