package com.eatz.customerservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatz.customerservice.dto.CustomerDTO;
import com.eatz.customerservice.dto.CustomerUpdateDTO;
import com.eatz.customerservice.dto.CustomerResponseDTO;
import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.service.customer.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ResponseEntity<String> registerCustomer(@RequestBody CustomerDTO user) {
		logger.debug("In registerCustomer method calling Customer Service");
		customerService.registerCustomer(user);
		logger.debug("Successfully registered the customer");
		return ResponseEntity.status(HttpStatus.OK).body("Successfully registered the customer");
	}

	@RequestMapping(path = "/customerDetails", method = RequestMethod.PUT)
	public ResponseEntity<String> updateCustomer(@RequestHeader String authorization,
			@RequestBody CustomerUpdateDTO user) {
		logger.debug("In updateCustomer method calling Customer Service");
		customerService.updateCustomer(user);
		logger.debug("Successfully updated customer details");
		return ResponseEntity.status(HttpStatus.OK).body("Successfully updated customer details");
	}

	@RequestMapping(path = "/customerById", method = RequestMethod.GET)
	public ResponseEntity<CustomerResponseDTO> getCustomerById(@RequestParam(name = "id") long id) {
		logger.debug("In getCustomerById method calling Customer Service");
		CustomerResponseDTO retrivedCustomer = customerService.getCustomerById(id);
		logger.debug("Customer found " + retrivedCustomer.getId() + "\t" + retrivedCustomer.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(retrivedCustomer);
	}

	@RequestMapping(path = "/customerByEmail", method = RequestMethod.GET)
	public ResponseEntity<LoginDTO> getCustomerByEmail(@RequestParam(name = "email") String email) {
		logger.debug("In getCustomerByName method calling Customer Service");
		LoginDTO retrivedCustomer = customerService.getCustomerByEmail(email);
		logger.debug("Customer found " + retrivedCustomer.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(retrivedCustomer);
	}

}
