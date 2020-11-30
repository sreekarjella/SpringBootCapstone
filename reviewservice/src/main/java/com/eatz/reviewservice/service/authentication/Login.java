package com.eatz.reviewservice.service.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatz.reviewservice.dto.LoginDTO;

@FeignClient(url = "http://localhost:8000/customer", name = "USER-LOGIN")
public interface Login {

	@GetMapping(path = "/customerByEmail")
	public LoginDTO loginCustomer(@RequestParam(name = "email") String email);
}
