package com.plans.service;


import com.plans.entity.Plan;

import java.util.List;

public interface PlanService {
	List<Plan> searchPlans();

	Plan save(Plan plan);
}
