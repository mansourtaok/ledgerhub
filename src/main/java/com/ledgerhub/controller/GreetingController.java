package com.ledgerhub.controller;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@RestController
public class GreetingController {

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@GetMapping("/greet")
	public String greet(@RequestParam("name") String name) {

		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		String base64Key = Encoders.BASE64.encode(key.getEncoded());
		System.out.println(base64Key);

		return "Hello, " + name + "!" + " - " + bcryptEncoder.encode("123456");
	}

	@GetMapping("/secure-endpoint")
	public String secureEndpoint(@RequestParam("name") String name) {

		return "Hello, " + name + "!";
	}
}
