package com.eatz.customerservice.service.customer;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eatz.customerservice.dto.CustomerDTO;
import com.eatz.customerservice.dto.CustomerUpdateDTO;
import com.eatz.customerservice.dto.CustomerResponseDTO;
import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.exception.CustomerException;
import com.eatz.customerservice.exception.CustomerNotFoundException;
import com.eatz.customerservice.model.Customer;
import com.eatz.customerservice.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomerRepository repository;

	private void validateCustomer(String firstName, String lastName, String email, String password) {
		logger.debug("Validating the customer details");
		if (!firstName.matches("[a-zA-Z]*")) {
			logger.debug("Customer first name cannot be accepted");
			throw new CustomerException("Invalid First Name");
		}
		if (!lastName.matches("[a-zA-Z]*")) {
			logger.debug("Customer last name cannot be accepted");
			throw new CustomerException("Invalid Last Name");
		}
		if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
			logger.debug("Customer email cannot be accepted");
			throw new CustomerException("Invalid email");
		}
		if (password.isEmpty()) {
			logger.debug("Customer password cannot be accepted");
			throw new CustomerException("Invalid Password");
		}
	}

	@Override
	public Customer registerCustomer(CustomerDTO user) {
		validateCustomer(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
		if (repository.findCustomerByEmail(user.getEmail()).isPresent()) {
			throw new CustomerException("Multiple accounts with same email is not permitted");
		}
		logger.debug("Customer passed the validations saving the details to database");
		Customer toPersist = new Customer();
		toPersist.setFirstname(user.getFirstName());
		toPersist.setLastname(user.getLastName());
		toPersist.setEmail(user.getEmail());
		toPersist.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(toPersist);
	}

	@Override
	public Customer updateCustomer(CustomerUpdateDTO user) {
		validateCustomer(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
		logger.debug("Customer passed the validations updating the details to database");
		Optional<Customer> checkDuplicate = repository.findCustomerByEmail(user.getEmail());
		if (!checkDuplicate.isPresent() || user.getId() != checkDuplicate.get().getId()) {
			throw new CustomerException("Provide proper ID of the user to update details");
		}
		Customer toPersist = buildCustomer(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getPassword());
		return repository.save(toPersist);
	}

	private Customer buildCustomer(long id, String firstName, String lastName, String email, String password) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setFirstname(firstName);
		customer.setLastname(lastName);
		customer.setEmail(email);
		customer.setPassword(passwordEncoder.encode(password));
		return customer;
	}

	@Override
	public CustomerResponseDTO getCustomerById(long id) {
		logger.debug("Finding customer with id " + id);
		Optional<Customer> customerRetrived = repository.findById(id);
		if (!customerRetrived.isPresent()) {
			logger.debug("No customer found with specified inputs");
			throw new CustomerNotFoundException("No customer found with specified inputs");
		}
		CustomerResponseDTO customer = new CustomerResponseDTO();
		customer.setId(customerRetrived.get().getId());
		customer.setFirstName(customerRetrived.get().getFirstname());
		customer.setLastName(customerRetrived.get().getLastname());
		customer.setEmail(customerRetrived.get().getEmail());
		return customer;
	}

	@Override
	public LoginDTO getCustomerByEmail(String email) {
		logger.debug("Finding customer with id " + email);
		Optional<Customer> customerRetrived = repository.findCustomerByEmail(email);
		if (!customerRetrived.isPresent()) {
			logger.debug("No customer found with specified inputs");
			throw new CustomerNotFoundException("No customer found with specified inputs");
		}
		LoginDTO loginDetails = new LoginDTO();
		loginDetails.setEmail(customerRetrived.get().getEmail());
		loginDetails.setPassword(customerRetrived.get().getPassword());
		return loginDetails;
	}

}
