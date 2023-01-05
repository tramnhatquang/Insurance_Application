package com.plans.service;

import com.plans.entity.Plan;
import com.plans.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
	@Autowired
	PlanRepository planRepository;

	@Override
	public List<Plan> searchPlans() {
		return planRepository.findAll();
	}

	@Override
	public Plan save(Plan plan) {
		return planRepository.save(plan);
	}

}
