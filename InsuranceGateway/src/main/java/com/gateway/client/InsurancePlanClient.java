package com.gateway.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InsurancePlanClient {
	public JsonNode searchPlans() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object> responseEntity = restTemplate.getForEntity("http://localhost:8383/searchPlans", Object.class);
		Object objects = responseEntity.getBody();

		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(objects, JsonNode.class);
	}

}
