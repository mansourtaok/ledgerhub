package com.ledgerhub.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserCreateDTO {

	private String email;

	private String password;

	private String abbreviation;
	private String preferredColor;

	private Long roleId;

	private Long preferredLangId;

}
