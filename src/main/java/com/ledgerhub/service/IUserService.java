package com.ledgerhub.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ledgerhub.model.db.User;
import com.ledgerhub.model.dto.user.UserRequestDTO;
import com.ledgerhub.model.dto.user.UserResponseDTO;

public interface IUserService {
	// TODO to use dto user instead of db user
	User findUserByEmail(String email);

	UserResponseDTO create(UserRequestDTO dto);

	UserResponseDTO update(Long id, UserRequestDTO dto);

	UserResponseDTO getById(Long id);

	List<UserResponseDTO> getAll();

	void delete(Long id);

	void hardDelete(Long id);

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
