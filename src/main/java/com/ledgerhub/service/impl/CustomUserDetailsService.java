package com.ledgerhub.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ledgerhub.service.IUserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final IUserService userService;

	public CustomUserDetailsService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userService.loadUserByUsername(username);
	}

}
