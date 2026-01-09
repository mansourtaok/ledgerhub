package com.ledgerhub.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.ledgerhub.model.db.User;

public interface IUserService {
	// TODO to use dto user instead of db user
	User findUserByEmail(String email);

	UserDetails loadUserByUsername(String username);
}
