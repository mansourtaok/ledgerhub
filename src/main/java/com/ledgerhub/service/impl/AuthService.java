package com.ledgerhub.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ledgerhub.config.jwt.JwtTokenUtil;
import com.ledgerhub.model.db.User;
import com.ledgerhub.model.dto.JwtRequest;
import com.ledgerhub.model.dto.JwtResponse;
import com.ledgerhub.service.IAuthService;

@Service
public class AuthService implements IAuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserService userService;

	public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			UserService userService) {

		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}

	@Override
	public JwtResponse authenticate(JwtRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

		String token = jwtTokenUtil.generateToken(userDetails);

		User user = userService.findUserByEmail(request.getUsername());
		return JwtResponse.from(user, token);
	}

}
