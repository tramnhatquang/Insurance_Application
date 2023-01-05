package com.plans;

import com.plans.entity.Benefit;
import com.plans.entity.Plan;
import com.plans.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class PlansTests {

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void testCreatePlan() {
		Plan p1 = new Plan("LifeWise Cascade Select Bronze", "LifeWise Essential plans are exclusive provider " +
				"organization (EPO) plans. \n" +
				"Care outside of your plan’s network is not covered, except for emergencies. \n" +
				"See next page for important plan information.", 6000, 8550, 244.85);

		Benefit b1 = testEntityManager.find(Benefit.class, 1);

		p1.addBenefit(b1);

		Plan savedP1 = planRepository.save(p1);
		assertThat(savedP1.getPlanId()).isGreaterThan(0);
	}

	@Test
	public void testCreateOtherPlans() {
		Plan p2 = new Plan("LifeWise Cascade Select Silver", "LifeWise Essential plans are exclusive provider " +
				"organization (EPO) plans. \n" +
				"Care outside of your plan’s network is not covered, except for emergencies. \n" +
				"See next page for important plan information.", 2500, 8500, 345.65);

		Plan p3 = new Plan("LifeWise Cascade Select Gold", "LifeWise Essential plans are exclusive provider " +
				"organization (EPO) plans. \n" +
				"Care outside of your plan’s network is not covered, except for emergencies. \n" +
				"See next page for important plan information.", 600, 5900, 413.91);

		Benefit b1 = testEntityManager.find(Benefit.class, 1);
		Benefit b2 = testEntityManager.find(Benefit.class, 2);
		Benefit b3 = testEntityManager.find(Benefit.class, 3);
		Benefit b4 = testEntityManager.find(Benefit.class, 4);
		Benefit b5 = testEntityManager.find(Benefit.class, 5);
		Benefit b6 = testEntityManager.find(Benefit.class, 6);



		p2.addBenefit(b1);
		p2.addBenefit(b2);
		p2.addBenefit(b3);
		p2.addBenefit(b4);

		p3.addBenefit(b1);
		p3.addBenefit(b2);
		p3.addBenefit(b3);
		p3.addBenefit(b4);
		p3.addBenefit(b5);
		p3.addBenefit(b6);

		Plan savedP2 = planRepository.save(p2);
		assertThat(savedP2.getPlanId()).isGreaterThan(0);

		Plan savedP3 = planRepository.save(p3);
		assertThat(savedP3.getPlanId()).isGreaterThan(0);
	}


}
