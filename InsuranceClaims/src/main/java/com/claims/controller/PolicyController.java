package com.claims.controller;

import com.claims.entity.Claim;
import com.claims.entity.Policy;
import com.claims.service.PolicyServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@RestController
public class PolicyController {
	@Autowired
	PolicyServiceImpl policyService;
	
	@RequestMapping(value="/createPolicy",method = RequestMethod.POST)
	public JsonNode createPolicy(@RequestBody JsonNode json) {
		JsonNode retNode = policyService.createPolicy(json);
		return retNode;
	}
	
	@RequestMapping(value="/getPolicy/{userName}",method = RequestMethod.GET)
	public List<Policy> getPolicy(@PathVariable String userName) {
		System.out.println(userName);
		return policyService.getPolicy(userName);
	}
	
	@RequestMapping(value="/getClaims",method = RequestMethod.GET)
	public List<Claim> getClaims() {
		return policyService.getClaims();
	}
	
	@RequestMapping(value="/approveClaim/{claimId}",method = RequestMethod.GET)
	public JsonNode apporveClaim(@PathVariable Long claimId) throws MessagingException {
		JsonNode retNode = policyService.approveClaim(claimId);
		return retNode;
	}
	
	@RequestMapping(value="/declineClaim/{claimId}",method = RequestMethod.GET)
	public JsonNode declineClaim(@PathVariable Long claimId) {
		return policyService.declineClaim(claimId);
	}
		
	@RequestMapping(value="/makeClaim",method = RequestMethod.POST)
	public JsonNode makeClaim(@RequestBody JsonNode json) throws IOException {
		System.out.println(json.get("policyId").asInt());
		JsonNode retNode = policyService.makeClaim(json);
		return retNode;
	}

	
	@RequestMapping(value="/sendPDF",method = RequestMethod.POST)
	public JsonNode sendPdf(@RequestBody JsonNode json) throws IOException, FileNotFoundException, MessagingException {
		String policyId = json.get("policyId").asText();
		String emailAdd = json.get("email").asText();
		String password = json.get("password").asText();
		String planName = json.get("planName").asText();
		String planType = json.get("planType").asText();
		String proposer = json.get("firstName").asText() + " " + json.get("lastName").asText();
		String physicalAddress = json.get("physicalAddress").asText();
		String billingAmount = "$"+json.get("billingAmount").asText();
		String filePath = "C:/temp/pdf/policy_"+policyId+".pdf";
		System.out.println(policyId);
		System.out.println(emailAdd);
        
        PdfWriter pdfWriter = new PdfWriter(filePath);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.LETTER);
        Document document = new Document(pdfDocument);
        float threeCol = 190f;
        float twoCol = 285f;
        float twoCol150 = twoCol+150f;
        float[] twoColWidth = {twoCol150, twoCol};
        float[] threeColWidth = {threeCol, threeCol, threeCol};
        float[] fullWidth = {threeCol*3};
        Paragraph oneSp = new Paragraph("\n");
        
        Table table = new Table(twoColWidth);
        table.addCell(new Cell().add("Receipt").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
        Table nestedTable = new Table(new float[]{twoCol/2,twoCol/2});
        nestedTable.addCell(new Cell().add("Policy Number :").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
        nestedTable.addCell(new Cell().add(policyId).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
        String strDate = formatter.format(date); 
        
        nestedTable.addCell(new Cell().add("Date :").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
        nestedTable.addCell(new Cell().add(strDate).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));
        
        Border border = new SolidBorder(Color.GRAY,2f);
        Table divider = new Table(fullWidth);
        divider.setBorder(border);
        document.add(table);
        document.add(divider.setMarginBottom(12f).setMarginTop(10f));
        
        Table twoColTable = new Table(twoColWidth);
        twoColTable.addCell(new Cell().add("Billing Informantion").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable.addCell(new Cell().add("Policy Information").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        document.add(twoColTable.setMarginBottom(12f));
        
        Table twoColTable2 = new Table(twoColWidth);
        twoColTable2.addCell(new Cell().add("Proposer").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add("Plan Name").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add(proposer).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add(planName).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add("Amount").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add("Policy Type").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add(billingAmount).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        twoColTable2.addCell(new Cell().add(planType).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        document.add(twoColTable2.setMarginLeft(12f));
            
        float[] oneColWidth = {twoCol150};
        Table oneColTable = new Table(oneColWidth);
        oneColTable.addCell(new Cell().add("Address").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        oneColTable.addCell(new Cell().add(physicalAddress).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        oneColTable.addCell(new Cell().add("Email").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        oneColTable.addCell(new Cell().add(emailAdd).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        document.add(oneColTable.setMarginLeft(12f));
        
        document.add(oneSp);
    	Table tableDivider2 = new Table(fullWidth);
    	Border border2 = new DashedBorder(Color.GRAY, 0.5f);
    	document.add(tableDivider2.setBorder(border2).setMarginBottom(12f));
    	
    	Paragraph p2 = new Paragraph("Billing Details");
    	document.add(p2.setBold().setMarginBottom(20f));
    	
    	Table threeColTable = new Table(threeColWidth);
    	threeColTable.setBackgroundColor(Color.BLACK, 0.7f);
    	
    	threeColTable.addCell(new Cell().add("Beneficiary").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
    	threeColTable.addCell(new Cell().add("Relation to Proposer").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
    	threeColTable.addCell(new Cell().add("Amount").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
    	
    	document.add(threeColTable);
    	float total = 0.0f;
    	String proposerAmount;
    	if (planName.equals("Bronze")) {
    		proposerAmount = "$250.6";
    		total += 250.6f;
    	}	else if (planName.equals("Silver")) {
    		proposerAmount = "$295.75";
    		total += 295.75f;
    	}	else {
    		proposerAmount = "$338.9";
    		total += 338.9f;
    	}
    	
    	Table threeColTable2 = new Table(threeColWidth);
    	threeColTable2.addCell(new Cell().add(proposer).setBorder(Border.NO_BORDER).setMarginLeft(10f));
    	threeColTable2.addCell(new Cell().add("(him/her)self").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
    	threeColTable2.addCell(new Cell().add(proposerAmount).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        
    	String memberName, relationToProposer;
    	Float memberAmount = 0.0f, propAmount = total;
    
		for(JsonNode i : json.get("members")) {
			memberName = i.get("firstName").asText() + " " + i.get("lastName").asText();
			relationToProposer = i.get("relationToProposer").asText();
			memberAmount = relationToProposer.equals("Spouse") ? propAmount*0.6f: propAmount*0.4f;
			
			total += memberAmount;
	    	threeColTable2.addCell(new Cell().add(memberName).setBorder(Border.NO_BORDER).setMarginLeft(10f));
	    	threeColTable2.addCell(new Cell().add(relationToProposer).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
	    	threeColTable2.addCell(new Cell().add("$"+String.format("%.2f",memberAmount)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
		}
        
        document.add(threeColTable2.setMarginBottom(20f));

    	
        Table threeColTable3 = new Table(twoColWidth);
        threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        threeColTable3.addCell(new Cell().add("----------------------------------------------------").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        document.add(threeColTable3);
               
        Table threeColTable4 = new Table(twoColWidth);
        threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        threeColTable4.addCell(new Cell().add("Total                                $"+String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        document.add(threeColTable4);

        document.close();
        
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
	    FileSystemResource file = new FileSystemResource(new File(filePath));
		message.setFrom("limjoon629@gmail.com");
		message.setTo(emailAdd);
		message.setSubject("Confirming your policy purchase");
		message.setText("Username: " + emailAdd + "\nPassword: " + password);
		message.addAttachment("policy_"+policyId+".pdf", file);
		mailSender.send(mimeMessage);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode();
		((ObjectNode) node).put("success", true);
		return node;
	}
	
}


