package com.ramit.demosecurity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicHelloController {

	@GetMapping("/basic/hello")
	public String hello(){
		return "Hello World after basic auth!";
	}
	
	@GetMapping("/admin/hello")
	public String adminOnly(){
		return "Hello admin !";
	}
	
	@GetMapping("/user/hello")
	public String userDetailsHello(){
		return "Hello user !";
	}
	
	@GetMapping("/user/getLoggedInUser")
	@ResponseBody
	public String getLoggedInUser(@AuthenticationPrincipal User user){
		return user.getUsername();
	}
}
