package com.ledgerhub.model.dto.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

	private Long id;
	private String email;
	private String abbreviation;
	private String preferredColor;

	private String role;
	private String preferredLang;

	private Boolean active;
	private LocalDateTime createdAt;

}
