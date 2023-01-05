package com.claims.repository;

import com.claims.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

	public Beneficiary findByBeneficiaryId(int id);
	List<Beneficiary> findByFirstNameAndLastNameAndGenderAndRelationToProposerAndDob(String i1, String i2, String i3, String i4, Timestamp i5);
}
