package com.ramit.demosecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ramit.demosecurity.security.BasicAuthEntryPoint;

@EnableWebSecurity
@Configuration
@Order(1)
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BasicAuthEntryPoint authEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/basic/**").hasRole("USER")
				.and()
				.httpBasic()
				.authenticationEntryPoint(authEntryPoint);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//Original passwords that have been encoded using bcrypt (http://www.devglan.com/online-tools/bcrypt-hash-generator)
		//for admin password 'adminpass' - $2a$04$fxNcMBR7eQtNFCLuRz7Ppen7ai5dkHNBoYjPhdZt8oeoC998qlbK.
		//for user password 'password' - $2a$04$AjFEmZeX7mN8zSn57PUEZeJgBeoKMvwteZMBiP57Jb4AGFsUORmLC
		auth.inMemoryAuthentication().withUser("admin").password("$2a$04$fxNcMBR7eQtNFCLuRz7Ppen7ai5dkHNBoYjPhdZt8oeoC998qlbK.").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("user").password("$2a$04$AjFEmZeX7mN8zSn57PUEZeJgBeoKMvwteZMBiP57Jb4AGFsUORmLC").roles("USER");
	}
	
	@Bean
	//Encoder use is mandatory from Spring 2 onwards
	public BCryptPasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}


}
