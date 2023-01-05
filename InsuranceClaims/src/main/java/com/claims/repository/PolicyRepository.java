package com.claims.repository;

import com.claims.entity.Policy;
import com.claims.entity.Proposer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

	public Policy findByPolicyId(long id);
	public List<Policy> findByProposer(Proposer p1);
}
