package com.ledgerhub.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.SystemLookup;
import com.ledgerhub.model.db.User;
import com.ledgerhub.model.dto.user.UserRequestDTO;
import com.ledgerhub.model.dto.user.UserResponseDTO;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.IUserService;

@Service

public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final SystemLookupRepository lookupRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, SystemLookupRepository lookupRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.lookupRepository = lookupRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Override
	public UserResponseDTO create(UserRequestDTO dto) {

		SystemLookup role = lookupRepository.findById(dto.getRoleId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));

		SystemLookup lang = dto.getPreferredLangId() != null
				? lookupRepository.findById(dto.getPreferredLangId()).orElse(null)
				: null;

		User user = User.builder().email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword()))
				.abbreviation(dto.getAbbreviation()).preferredLang(lang).preferredColor(dto.getPreferredColor())
				.role(role).build();

		return toDto(userRepository.save(user));
	}

	@Override
	public UserResponseDTO update(Long id, UserRequestDTO dto) {

		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

		SystemLookup role = lookupRepository.findById(dto.getRoleId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));

		SystemLookup lang = dto.getPreferredLangId() != null
				? lookupRepository.findById(dto.getPreferredLangId()).orElse(null)
				: null;

		user.setEmail(dto.getEmail());
		user.setAbbreviation(dto.getAbbreviation());
		user.setPreferredColor(dto.getPreferredColor());
		user.setPreferredLang(lang);
		user.setRole(role);

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		return toDto(userRepository.save(user));

	}

	@Override
	public UserResponseDTO getById(Long id) {
		return userRepository.findById(id).map(this::toDto)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

	@Override
	public List<UserResponseDTO> getAll() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), false).map(this::toDto).toList();
	}

	@Override
	public void delete(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public void hardDelete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new IllegalArgumentException("User not found");
		}
		userRepository.deleteById(id);
	}

	private UserResponseDTO toDto(User user) {
		return UserResponseDTO.builder().id(user.getId()).email(user.getEmail()).abbreviation(user.getAbbreviation())
				.preferredColor(user.getPreferredColor()).role(user.getRole().getCode())
				.preferredLang(user.getPreferredLang() != null ? user.getPreferredLang().getCode() : null)
				.active(user.getActive()).createdAt(user.getCreatedAt()).build();
	}

}
