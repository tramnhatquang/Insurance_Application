package com.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebMVCController {


	@GetMapping("/home")
	public String viewHomePage() {
		return "home";
	}





}
