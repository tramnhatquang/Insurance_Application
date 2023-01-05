package com.claims.service;

import com.claims.entity.*;
import com.claims.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PolicyServiceImpl implements PolicyService {
	@Autowired
	BeneficiaryRepository beneficiaryRepository;
	
	@Autowired
	PolicyRepository policyRepository;
	
	@Autowired
	ProposerRepository proposerRepository;
	
	@Autowired
	ClaimRepository claimRepository;
	
	@Autowired
	ReasonRepository reasonRepository;
	
	@Override
	public JsonNode createPolicy(JsonNode json) {
		int planId = json.get("planId").asInt();
		String planName = json.get("planName").asText();
		String firstName = json.get("firstName").asText();
		String lastName = json.get("lastName").asText();
		String gender = json.get("gender").asText();
		String email = json.get("email").asText();
		String phoneNo = json.get("phoneNo").asText();
		String address = json.get("address").asText();
		String state = json.get("state").asText();
		String zipCode = json.get("zipCode").asText();
		double monthlyPrice = json.get("monthlyPrice").asDouble();
		
		String dob = json.get("dob").asText();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateOfB;
		Timestamp dateOfBirth = null;
		try {
			dateOfB = format.parse(dob);
			dateOfBirth = new Timestamp(dateOfB.getTime());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(gender.equals("Male")) {
			gender = "MALE";
		}	else {
			gender = "FEMALE";
		}
		Proposer p1 = proposerRepository.findByFirstNameAndLastNameAndGenderAndDobAndEmailAndZipCode(firstName, lastName, gender, dateOfBirth, email, zipCode);
		Proposer p2 = new Proposer();
		
		if (p1 == null) {
			p2.setDob(dateOfBirth);
			p2.setAddress(address);
			p2.setEmail(email);
			p2.setFirstName(firstName);
			p2.setLastName(lastName);
			p2.setGender(gender);
			p2.setPhoneNo(phoneNo);
			p2.setState(state);
			p2.setZipCode(zipCode);
			proposerRepository.save(p2);
		}
		
		
		Set<Beneficiary> benList = new HashSet<>();
		String fName,lName,gen,relationToProposer,dob2;
		for(JsonNode i : json.get("beneficiaries")) {
			fName = i.get("firstName").asText();
			lName = i.get("lastName").asText();
			gen = i.get("gender").asText();
			relationToProposer = i.get("relationToProposer").asText();
			dob2 = i.get("dateOfBirth").asText();
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			Date dateOfB2;
			Timestamp dateOfBirth2 = null;
			try {
				dateOfB2 = format2.parse(dob2);
				dateOfBirth2 = new Timestamp(dateOfB2.getTime());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(fName + " " + lName + " " + gen + " " + relationToProposer + " " + dateOfBirth2);
			List<Beneficiary> tempList = beneficiaryRepository.findByFirstNameAndLastNameAndGenderAndRelationToProposerAndDob(fName, lName, gen, relationToProposer, dateOfBirth2);
			if(tempList.isEmpty()) {
				Beneficiary ben1 = new Beneficiary();
				ben1.setFirstName(fName);
				ben1.setLastName(lName);
				ben1.setGender(gen);
				ben1.setDob(dateOfBirth2);
				if (p1 != null) {
					ben1.setProposerId(p1.getProposerId());
				}	else {
					ben1.setProposerId(p2.getProposerId());
				}
				ben1.setRelationToProposer(relationToProposer);
				beneficiaryRepository.save(ben1);
				benList.add(ben1);
			} 	else {
				Iterator<Beneficiary> tempIt = tempList.iterator();
				while(tempIt.hasNext()) {
					benList.add(tempIt.next());
				}
			}
		}
		
		int noOfAdults = 1;
		int noOfChildren = 0;
		if (!benList.isEmpty()) {
			double addition = 0.0;
			Iterator<Beneficiary> benIt = benList.iterator();
			while(benIt.hasNext()) {
				String rel = benIt.next().getRelationToProposer();
				if (rel.equalsIgnoreCase("Spouse")) {
					addition += monthlyPrice*0.6;
					noOfAdults += 1;
				}	else if (rel.equalsIgnoreCase("Children")){
					addition += monthlyPrice*0.4;
					noOfChildren += 1;
				}
			}
			monthlyPrice+= addition;
		}
		
		Policy policy = new Policy();
		policy.setPlanId(planId);
		policy.setPlanName(planName);
		if (p1 != null) {
			policy.setProposer(p1);
		}	else {
			policy.setProposer(p2);
		}
		policy.setNoOfAdults(noOfAdults);
		policy.setNoOfChildren(noOfChildren);
		policy.setMonthlyPrice(monthlyPrice);
		policy.setCurrDeductible(monthlyPrice);
		policy.setBeneficiaries(benList);
		policy.setOutOfPocket(monthlyPrice);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode();
		
		List<Policy> duplicate = new ArrayList<>();		
		if (p1 != null) {
			duplicate = policyRepository.findByProposer(p1);
		}	else {
			duplicate = policyRepository.findByProposer(p2);
		}
		
		for (Policy p: duplicate) {
			if (p.getBeneficiaries().equals(policy.getBeneficiaries()) && p.getProposer().equals(policy.getProposer())
				&& p.getPlanName().equals(policy.getPlanName())) {
				((ObjectNode) node).put("policyId", policy.getPolicyId());
				((ObjectNode) node).put("totalPrice", monthlyPrice);
				((ObjectNode) node).put("success", false);
				return node;
			}
		}
		
		
		policyRepository.save(policy);
		((ObjectNode) node).put("policyId", policy.getPolicyId());
		((ObjectNode) node).put("totalPrice", monthlyPrice);
		((ObjectNode) node).put("success", true);
		return node;
	}

	@Override
	public List<Policy> getPolicy(String userName) {
		System.out.println("user Email: "+ userName);
		List<Proposer> proposers = proposerRepository.findByEmail(userName);
		List<Policy> ret = new ArrayList<>();
		
		for (Proposer p1 : proposers) {
			List<Policy> policies = policyRepository.findByProposer(p1);
			ret.addAll(policies);
		}
		
		return ret;
	}

	@Override
	public JsonNode makeClaim(JsonNode json) throws IOException {
		Long policyId = json.get("policyId").asLong();
		String polIdText = policyId.toString();
		String claimee = json.get("claimee").asText();
		String dob = json.get("dob").asText();
		String hospitalName = json.get("hospitalName").asText();
		String email = json.get("email").asText();
		String description = json.get("description").asText();
		String phoneNum = json.get("phoneNo").asText();
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// String formatted = format.format(System.currentTimeMillis());
		Date dateOfB;
		Timestamp dateOfBirth = null;
		try {
			dateOfB = format.parse(dob);
			dateOfBirth = new Timestamp(dateOfB.getTime());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Policy p1 = policyRepository.findByPolicyId(policyId);
		String planName = p1.getPlanName();
		Claim c1 = new Claim();
		
		Set<Reason> reasonSet = c1.getReasonSet();
		Double claimable = 0.0, remainder = 0.0, totalClaim = 0.0, pocketCount = 0.0;
		for(JsonNode i : json.get("reasons")) {
			claimable = i.get("amount").asDouble();
	
			if (planName.equals("Bronze")) {
				if (p1.getOutOfPocket() < 9100) {
					remainder = claimable - (9100-p1.getOutOfPocket());
					if (remainder > 0) {
						pocketCount += 9100-p1.getOutOfPocket();
						claimable = remainder;
						p1.setOutOfPocket(9100);						
					}	else {
						p1.setOutOfPocket(p1.getOutOfPocket()+claimable);
						pocketCount += claimable;
						claimable = 0.0;
					}					
				}
			}	else if (planName.equals("Silver")) {
				if (p1.getCurrDeductible() < 5800 && p1.getOutOfPocket() < 8900) {
					remainder = claimable - (8900-p1.getOutOfPocket());
					if (remainder > 0) {
						pocketCount += 8900-p1.getOutOfPocket();						
						claimable = remainder;
						p1.setOutOfPocket(8900);
					}	else {
						p1.setOutOfPocket(p1.getOutOfPocket()+claimable);
						pocketCount += claimable;
						claimable = 0.0;
					}					
				}	else if (p1.getCurrDeductible() >= 5800 && p1.getOutOfPocket() < 8900) {
					remainder = claimable*0.4 - (8900-p1.getOutOfPocket());
					if (remainder > 0) {
						pocketCount += 8900-p1.getOutOfPocket();
						claimable = (8900-p1.getOutOfPocket())*(10.0/4.0)*0.6 + remainder*(10.0/4.0);
						p1.setOutOfPocket(8900);
					}	else {
						p1.setOutOfPocket(p1.getOutOfPocket()+claimable*0.4);
						pocketCount += claimable*0.4;
						claimable *= 0.6;
					}	
				}	
			}	else {
				if (p1.getCurrDeductible() < 2000 && p1.getOutOfPocket() < 8700) {
					remainder = claimable*0.5 - (8700-p1.getOutOfPocket());
					if (remainder > 0) {
						pocketCount += 8700-p1.getOutOfPocket();
						claimable = (8700-p1.getOutOfPocket())*2*0.5 + remainder*2;
						p1.setOutOfPocket(8700);
					}	else {
						p1.setOutOfPocket(p1.getOutOfPocket()+claimable*0.5);
						pocketCount += claimable*0.5;
						claimable *= 0.5;
					}					
				}	else if (p1.getCurrDeductible() >= 2000 && p1.getOutOfPocket() < 8700) {
					remainder = claimable*0.3 - (8700-p1.getOutOfPocket());
					if (remainder > 0) {
						pocketCount += 8700-p1.getOutOfPocket();
						claimable = (8700-p1.getOutOfPocket())*(10.0/3.0)*0.7 + remainder*(10.0/3.0);
						p1.setOutOfPocket(8700);
					}	else {
						p1.setOutOfPocket(p1.getOutOfPocket()+claimable*0.3);
						pocketCount += claimable*0.3;
						claimable *= 0.7;
					}
				}					
			}
			
			Reason reason1 = reasonRepository.findByDescription(i.get("reason").asText());
			
			if (reason1 == null) {
				reason1 = new Reason();
				reason1.setDescription(i.get("reason").asText());
				reasonRepository.save(reason1);
			}
			
			reasonSet.add(reason1);
			totalClaim += claimable;			
		}
		
		c1.setClaimee(claimee);
		c1.setReasonSet(reasonSet);
		c1.setTotalClaim(totalClaim);
		c1.setPocketCount(pocketCount);
		c1.setEmail(email);
		c1.setHospitalName(hospitalName);
		c1.setPhoneNum(phoneNum);
		c1.setPolicyId(policyId);
		c1.setStatus("pending");
		c1.setMainReason(description);
		c1.setDob(dateOfBirth);
		c1.setPlanName(planName);
		claimRepository.save(c1);
		
		Set<Claim> claims = p1.getClaims();
		claims.add(c1);
		p1.setClaims(claims);
		policyRepository.save(p1);
		
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode();
		((ObjectNode) node).put("success", true);
		((ObjectNode) node).put("claimId", c1.getClaimId());
		return node;
	}

	@Override
	public List<Claim> getClaims() {
		return claimRepository.findAll();
	}

	@Override
	public JsonNode approveClaim(Long claimId) throws MessagingException {
		Claim claim = claimRepository.findByClaimId(claimId);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format.format(System.currentTimeMillis());
		
		Date currDate;
		Timestamp currentTime = null;
		try {
			currDate = format.parse(formatted);
			System.out.println(currDate);
			currentTime = new Timestamp(currDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		claim.setDateOfAdmission(currentTime);	
		claim.setStatus("approved");
		claimRepository.save(claim);	
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    mailSender.setUsername("limjoon629@gmail.com");
	    mailSender.setPassword("dmlwcoixpgzpdufr");

	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setFrom("limjoon629@gmail.com");
		message.setTo(claim.getEmail());
		message.setSubject("Regarding Your claim "+claimId);
		message.setText("Congratulations! Your Claim has been approved");
		mailSender.send(mimeMessage);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode();
		((ObjectNode) node).put("status", "Claim approved!");
		return node;
	}
	
	@Override
	public JsonNode declineClaim(Long claimId) {
		
		Claim claim = claimRepository.findByClaimId(claimId);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode();
		
		if (claim == null) {
			((ObjectNode) node).put("status", "Claim does not exist!");
			return node;
		}
		
		Policy p1 = policyRepository.findByPolicyId(claim.getPolicyId());
		
		Set<Claim> claimSet = p1.getClaims();
		claimSet.remove(claim);

		Double retDec = p1.getOutOfPocket()-claim.getPocketCount();
		BigDecimal bd = new BigDecimal(retDec).setScale(2, RoundingMode.HALF_UP);  
		double newNum = bd.doubleValue();  
		
		p1.setClaims(claimSet);
		p1.setOutOfPocket(newNum);
		policyRepository.save(p1);
		claimRepository.delete(claim);
		
		((ObjectNode) node).put("status", "Claim declined!");
		return node;
	}

}
