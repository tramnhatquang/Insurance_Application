package com.plans.controller;

import com.plans.entity.Plan;
import com.plans.service.PlanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanController {
	@Autowired
	private PlanServiceImpl planService;

	@GetMapping(value="/searchPlans")
	public List<Plan> searchPlans() {
		return planService.searchPlans();
	}

	
}
