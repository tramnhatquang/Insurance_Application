package com.claims.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Policy {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long policyId;
	private int planId;
	private String planName;
	private int noOfAdults; 
	private int noOfChildren;
	private double monthlyPrice;
	private double currDeductible;
	private double outOfPocket;
	
	@ManyToOne
	private Proposer proposer;
	@ManyToMany
	private Set<Beneficiary> beneficiaries;
	@OneToMany
	private Set<Claim> claims;
	

	public long getPolicyId() {
		return policyId;
	}
	public void setPolicyId(long policyId) {
		this.policyId = policyId;
	}
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public int getNoOfAdults() {
		return noOfAdults;
	}
	public void setNoOfAdults(int noOfAdults) {
		this.noOfAdults = noOfAdults;
	}
	public int getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(int noOfChildren) {
		this.noOfChildren = noOfChildren;
	}
	public double getMonthlyPrice() {
		return monthlyPrice;
	}
	public void setMonthlyPrice(double monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	public Proposer getProposer() {
		return proposer;
	}
	public void setProposer(Proposer proposer) {
		this.proposer = proposer;
	}
	public Set<Beneficiary> getBeneficiaries() {
		return beneficiaries;
	}
	public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}
	
	public Set<Claim> getClaims() {
		return claims;
	}
	public void setClaims(Set<Claim> claims) {
		this.claims = claims;
	}
	public double getCurrDeductible() {
		return currDeductible;
	}
	public void setCurrDeductible(double currDeductible) {
		this.currDeductible = currDeductible;
	}
	public double getOutOfPocket() {
		return outOfPocket;
	}
	public void setOutOfPocket(double outOfPocket) {
		this.outOfPocket = outOfPocket;
	}	
}
