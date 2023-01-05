package com.claims.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Claim {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long claimId;
	private long policyId;
	private String planName;
	private String claimee;
	private Timestamp dob;
	private Timestamp dateOfAdmission;
	private String hospitalName;
	private String phoneNum;
	private String email;
	private String mainReason;
	private String status;
	
	@ManyToMany
	private Set<Reason> reasonSet = new HashSet<>();
	
	private double pocketCount;
	private double totalClaim;


	public long getClaimId() {
		return claimId;
	}

	public void setClaimId(long claimId) {
		this.claimId = claimId;
	}

	public long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(long policyId) {
		this.policyId = policyId;
	}

	public String getClaimee() {
		return claimee;
	}

	public void setClaimee(String claimee) {
		this.claimee = claimee;
	}

	public Timestamp getDob() {
		return dob;
	}

	public void setDob(Timestamp dob) {
		this.dob = dob;
	}

	public Timestamp getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Timestamp dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMainReason() {
		return mainReason;
	}

	public void setMainReason(String mainReason) {
		this.mainReason = mainReason;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public double getTotalClaim() {
		return totalClaim;
	}

	public void setTotalClaim(double totalClaim) {
		this.totalClaim = totalClaim;
	}

	public Set<Reason> getReasonSet() {
		return reasonSet;
	}

	public void setReasonSet(Set<Reason> reasonSet) {
		this.reasonSet = reasonSet;
	}

	public double getPocketCount() {
		return pocketCount;
	}

	public void setPocketCount(double pocketCount) {
		this.pocketCount = pocketCount;
	}
	
	
}
