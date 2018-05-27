package com.ramit.demosecurity.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ramit.demosecurity.security.CustomBasicAuthenticationEntryPoint;
import com.ramit.demosecurity.security.JwtAuthenticationEntryPoint;
import com.ramit.demosecurity.security.JwtAuthenticationProvider;
import com.ramit.demosecurity.security.JwtAuthenticationTokenFilter;
import com.ramit.demosecurity.security.JwtSuccessHandler;

@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
@Configuration
@Order(1)
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtAuthenticationProvider authenitcationProvider;
	
	@Autowired
	private JwtAuthenticationEntryPoint entryPoint;
	
	@Bean
	public AuthenticationManager authenticationManager(){
		return new ProviderManager(Collections.singletonList(authenitcationProvider));
	}
	
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilter(){
		JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManager());
		authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		return authenticationTokenFilter;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		 //.anyRequest() would authorize all requests
		  http.csrf().disable()
          .authorizeRequests().antMatchers("**/rest/**").authenticated()
          .and()
          .exceptionHandling().authenticationEntryPoint(entryPoint)
          .and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		  http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		  http.headers().cacheControl();
	}
	


}
