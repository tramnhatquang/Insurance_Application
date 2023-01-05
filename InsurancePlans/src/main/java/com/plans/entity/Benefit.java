package com.plans.entity;

import javax.persistence.*;

@Entity
public class Benefit {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int benefitId;
    @Column(nullable = false, unique = true)
	private String benefitName;
    @Column(nullable = false)
	private String benefitDescription;
	@Column(nullable = false)
	private double copay;

	@Column(nullable = false)
    private double coInsurance;


	public Benefit() {
	}

	public Benefit(String benefitName, String benefitDescription, double copay, double coInsurance) {
		this.benefitName = benefitName;
		this.benefitDescription = benefitDescription;
		this.copay = copay;
		this.coInsurance = coInsurance;
	}

	@Override
	public String toString() {
		return "Benefit{" +
				"benefitName='" + benefitName + '\'' +
				", benefitDescription='" + benefitDescription + '\'' +
				", copay=" + copay +
				", coInsurance=" + coInsurance +
				'}';
	}

	public int getBenefitId() {
		return benefitId;
	}
	public void setBenefitId(int benefitId) {
		this.benefitId = benefitId;
	}
	public String getBenefitName() {
		return benefitName;
	}
	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}
	public String getBenefitDescription() {
		return benefitDescription;
	}
	public void setBenefitDescription(String benefitDescription) {
		this.benefitDescription = benefitDescription;
	}
	public double getCopay() {
		return copay;
	}
	public void setCopay(double copay) {
		this.copay = copay;
	}
	public double getCoInsurance() {
		return coInsurance;
	}
	public void setCoInsurance(double coInsurance) {
		this.coInsurance = coInsurance;
	}



}

