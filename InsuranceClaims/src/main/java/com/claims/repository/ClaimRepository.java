package com.claims.repository;

import com.claims.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long>{
	public Claim findByClaimId(long id);
}
