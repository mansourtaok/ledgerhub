package com.ledgerhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.JwtRequest;
import com.ledgerhub.model.dto.JwtResponse;
import com.ledgerhub.service.IAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthController {

	private final IAuthService authService;

	public AuthController(IAuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "Login")
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
}
