package com.plans;

import com.plans.entity.Benefit;
import com.plans.repository.BenefitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BenefitsTest {

	@Autowired
	private BenefitRepository benefitRepository;

	@Test
	public void createFirstBenefitsTest() {
		Benefit b1 = new Benefit("Ambulatory patient services", "Spinal manipulation: 10 visits PCY; \n" +
				"Acupuncture: 12 visits PCY", 100, 20);

		Benefit savedB1 = benefitRepository.save(b1);
		assertThat(savedB1.getBenefitId()).isGreaterThan(0);
	}

	@Test
	public void creatMultipleBenefitTests() {
		Benefit b2 = new Benefit("Emergency services", "Emergency care (copay waived if directly \n" +
				"admitted to an inpatient facility)", 1000, 15);
		Benefit b3 = new Benefit("Hospitalization", "Organ and tissue transplants, inpatient", 2000, 15);
		Benefit b4 = new Benefit("Maternity and newborn care", "Prenatal and postnatal care", 2500, 30);
		Benefit b5 = new Benefit("Laboratory services", "Includes x-ray, pathology, imaging and \n" +
				"diagnostic, standard ultrasound. Major imaging, including MRI, CT, PET\n" +
				"(preapproval required for certain services)", 5500, 30);

		Benefit b6 = new Benefit("Virtual care", "Doctor On Demand: general medicine. Boulder Care or Workit Health: " +
				"Mental health \n" +
				"including substance use disorder", 50, 12);
		benefitRepository.saveAll(List.of(b2, b3, b4, b5, b6));
	}
}
