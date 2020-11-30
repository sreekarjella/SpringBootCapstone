package com.eatz.customerservice.service.customer;

import com.eatz.customerservice.dto.CustomerDTO;
import com.eatz.customerservice.dto.CustomerUpdateDTO;
import com.eatz.customerservice.dto.CustomerResponseDTO;
import com.eatz.customerservice.dto.LoginDTO;
import com.eatz.customerservice.model.Customer;

public interface CustomerService {

	Customer registerCustomer(CustomerDTO user);

	Customer updateCustomer(CustomerUpdateDTO user);

	CustomerResponseDTO getCustomerById(long id);

	LoginDTO getCustomerByEmail(String username);

}
