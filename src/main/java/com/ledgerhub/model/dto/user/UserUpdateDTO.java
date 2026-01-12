package com.ledgerhub.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserUpdateDTO {

	private String abbreviation;
	private String preferredColor;

	private Boolean active;
	private Long roleId;
	private Long preferredLangId;

}
