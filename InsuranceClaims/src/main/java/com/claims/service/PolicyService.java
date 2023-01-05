package com.claims.service;

import com.claims.entity.Claim;
import com.claims.entity.Policy;
import com.fasterxml.jackson.databind.JsonNode;


import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface PolicyService {
	JsonNode createPolicy(JsonNode json);
	JsonNode makeClaim(JsonNode json)throws IOException;
	JsonNode approveClaim(Long claimId) throws MessagingException;
	List<Policy> getPolicy(String userName);
	List<Claim> getClaims();
	JsonNode declineClaim(Long claimId);
}
