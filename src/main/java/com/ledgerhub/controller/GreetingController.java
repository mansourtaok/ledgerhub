package com.ledgerhub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@GetMapping("/greet")
	public String greet(@RequestParam("name") String name) {
		return "Hello, " + name + "!";
	}
}
