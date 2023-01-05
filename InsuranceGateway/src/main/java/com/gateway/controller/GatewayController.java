package com.gateway.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.gateway.client.InsurancePlanClient;
import com.gateway.repository.UserRepository;
import com.gateway.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

	@Autowired
	InsurancePlanClient insurancePlanClient;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/searchPlans")
	public JsonNode searchPlans() {
		System.out.println("Inside the GATEWAY search plan ");
		return insurancePlanClient.searchPlans();
	}

}
