package com.plans.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Plan {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long planId;

	@Column(nullable = false, unique = true)
	private String planName;
	@Column(nullable = false)
	private String description;
	@Column(nullable = false)
	private double deductible;
	@Column(nullable = false)
	private double maxOutOfPocket;
	@Column(nullable = false)
	private double monthlyPrice;
	@ManyToMany
	@JoinTable(name = "plans_benefits",
			joinColumns = @JoinColumn(name = "plan_id"),
			inverseJoinColumns = @JoinColumn(name = "benefit_id"))
	private Set<Benefit> benefits = new HashSet<>();

	public void addBenefit(Benefit benefit) {
		this.benefits.add(benefit);
	}
	public Plan() {
	}

	public Plan(String planName, String description, double deductible, double maxOutOfPocket, double monthlyPrice) {
		this.planName = planName;
		this.description = description;
		this.deductible = deductible;
		this.maxOutOfPocket = maxOutOfPocket;
		this.monthlyPrice = monthlyPrice;
	}

	@Override
	public String toString() {
		return "Plan{" +
				"planName='" + planName + '\'' +
				", description='" + description + '\'' +
				", deductible=" + deductible +
				", maxOutOfPocket=" + maxOutOfPocket +
				", monthlyPrice=" + monthlyPrice +
				'}';
	}

	public long getPlanId() {
		return planId;
	}
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getDeductible() {
		return deductible;
	}
	public void setDeductible(double deductible) {
		this.deductible = deductible;
	}
	public double getMaxOutOfPocket() {
		return maxOutOfPocket;
	}
	public void setMaxOutOfPocket(double maxOutOfPocket) {
		this.maxOutOfPocket = maxOutOfPocket;
	}
	public Set<Benefit> getBenefits() {
		return benefits;
	}
	public void setBenefits(Set<Benefit> benefits) {
		this.benefits = benefits;
	}
	public double getMonthlyPrice() {
		return monthlyPrice;
	}
	public void setMonthlyPrice(double monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	
}

