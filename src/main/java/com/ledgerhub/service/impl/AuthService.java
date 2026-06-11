package com.ledgerhub.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ledgerhub.config.jwt.JwtTokenUtil;
import com.ledgerhub.model.db.User;
import com.ledgerhub.model.dto.JwtRequest;
import com.ledgerhub.model.dto.JwtResponse;
import com.ledgerhub.repository.UserCompanyRepository;
import com.ledgerhub.service.IAuthService;

@Service
public class AuthService implements IAuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserService userService;
	private final UserCompanyRepository userCompanyRepository;

	public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			UserService userService, UserCompanyRepository userCompanyRepository) {

		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
		this.userCompanyRepository = userCompanyRepository;
	}

	@Override
	public JwtResponse authenticate(JwtRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

		User user = userService.findUserByEmail(request.getUsername());
		String token = jwtTokenUtil.generateToken(user.getId(), userDetails);

		Long defaultCompanyId = userCompanyRepository.findByUserIdAndIsDefaultTrue(user.getId())
				.map(uc -> uc.getCompany().getId())
				.orElse(null);

		return JwtResponse.from(user, token, defaultCompanyId);
	}

}
