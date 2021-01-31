package com.ramit.demosecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@Order(3)
public class BasicUserDetailsConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;//Created in BasicSecurityConfig.java

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
	      .csrf().disable()
	      .authorizeRequests()
	        .antMatchers("/user/**").hasAnyRole("USER")
	        .and()
	      .formLogin() //No auth check for login form, in case you have one
	        .loginPage("/login")
	        .defaultSuccessUrl("/dashboard")
	        .permitAll();
	 }

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
	}


}
