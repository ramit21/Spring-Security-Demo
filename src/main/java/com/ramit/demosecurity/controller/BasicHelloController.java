package com.ramit.demosecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/basic/hello")
public class BasicHelloController {

	@GetMapping
	public String hello(){
		return "Hello World after basic auth!";
	}
}
