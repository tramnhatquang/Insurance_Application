package com.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;


@Controller
public class UserController {
		
	@Autowired
	PasswordEncoder passwordEncoder;

	
	@RequestMapping(value="/login")
	public String login(@RequestParam(required=false) String logout, @RequestParam(required=false) String error, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Model model) {
		String message = "";
		if(error!=null) {
			message="Invalid Credentials";
		}
		if(logout!=null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth!=null) {
				new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
			}
		}
		model.addAttribute("message", message);
		return "login";
		
	}

	
	@RequestMapping(value="/accessDeniedPage")
	public String accessDenied(Principal principal, Model model) {
		String message = principal.getName()+", Unauthorised access";
		model.addAttribute("Message", message);
		return "accessDeniedPage";
		
	}
	
}
