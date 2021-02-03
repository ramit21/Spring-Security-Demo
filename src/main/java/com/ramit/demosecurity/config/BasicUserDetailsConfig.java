package com.ramit.demosecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
		securedEnabled=true, // to enable @Secured
		prePostEnabled = true // to enable @PreAuthorize
		) 
@Order(3)
public class BasicUserDetailsConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;//Created in BasicSecurityConfig.java

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
	      .csrf().disable() //To enable csrf, remove this line
	      .authorizeRequests()
	      .antMatchers("/","static/css").permitAll() //Allow public access to root page, and static content
	      .antMatchers("/user/**").hasAnyRole("USER")
	      .and()
	      .formLogin() //No auth check for login form, in case you have one
	        .loginPage("/login") //this is just for code demo, we dont have it configured in this poc yet
	        .defaultSuccessUrl("/dashboard")
	        .permitAll()
	       .and()
	       .sessionManagement()
	         .maximumSessions(2); //Maximum 2 active sessions for this user at max
	 }

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
	}


}
