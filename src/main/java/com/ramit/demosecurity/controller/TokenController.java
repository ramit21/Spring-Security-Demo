package com.ramit.demosecurity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramit.demosecurity.model.JwtUser;
import com.ramit.demosecurity.security.JwtGenerator;

@RestController
@RequestMapping(value="/token")
public class TokenController {

	//@Autowired
	private JwtGenerator jwtGenerator;
	
	public TokenController(JwtGenerator jwtGenerator){
		this.jwtGenerator = jwtGenerator;
	}
	
	@PostMapping
	public String generateToken(@RequestBody final JwtUser jwtUser){
		return jwtGenerator.generate(jwtUser);
	}
}
