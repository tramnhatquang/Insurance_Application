package com.claims.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Beneficiary {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int beneficiaryId;

	@Column(nullable = false, length = 20)
	private String firstName;
	@Column(nullable = false, length = 20)
	private String lastName;
	@Column(nullable = false, length = 20)
	private String gender;
	@Column(nullable = false)
	private Timestamp dob;
	private int proposerId;
	private String relationToProposer;
	
	public int getBeneficiaryId() {
		return beneficiaryId;
	}
	public void setBeneficiaryId(int beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Timestamp getDob() {
		return dob;
	}
	public void setDob(Timestamp dob) {
		this.dob = dob;
	}
	public int getProposerId() {
		return proposerId;
	}
	public void setProposerId(int proposerId) {
		this.proposerId = proposerId;
	}
	public String getRelationToProposer() {
		return relationToProposer;
	}
	public void setRelationToProposer(String relationToProposer) {
		this.relationToProposer = relationToProposer;
	}
}

