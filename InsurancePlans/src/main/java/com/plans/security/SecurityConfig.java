package com.plans.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(HttpSecurity hts) throws Exception {
		hts.authorizeRequests().anyRequest().permitAll();
//		hts.authorizeRequests().anyRequest().authenticated()
//				.and()
//				.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/home", true)
//				.and()
//				.logout().logoutSuccessUrl("/login?logout")
//				.and()
//				.exceptionHandling().accessDeniedPage("/accessDeniedPage")
//				.and()
//				.httpBasic()
//				.and()
//				.csrf().disable();
	}
}
