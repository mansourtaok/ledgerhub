package com.ledgerhub.model.dto.user;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserResponseDTO {

	private Long id;
	private String email;
	private String abbreviation;
	private String preferredColor;
	private String preferredLang;

	private Boolean active;
	private Long roleId;
	private Long preferredLangId;
	private LocalDateTime createdAt;

}
