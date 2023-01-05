package com.claims.repository;

import com.claims.entity.Proposer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ProposerRepository extends JpaRepository<Proposer, Long> {

	public Proposer findByFirstNameAndLastNameAndGenderAndDobAndEmailAndZipCode(String i1, String i2, String i3, Timestamp i4, String i5, String i6);
	public List<Proposer> findByEmail(String i1);
}
