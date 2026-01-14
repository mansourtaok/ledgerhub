package com.ledgerhub.model.mapper.user;

import org.springframework.stereotype.Component;

import com.ledgerhub.model.db.User;
import com.ledgerhub.model.dto.user.UserResponseDTO;

@Component
public class UserMapper {

	public UserResponseDTO toDto(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setAbbreviation(user.getAbbreviation());
		dto.setPreferredColor(user.getPreferredColor());
		dto.setActive(user.getActive());
		dto.setCreatedAt(user.getCreatedAt());

		return dto;
	}
}
