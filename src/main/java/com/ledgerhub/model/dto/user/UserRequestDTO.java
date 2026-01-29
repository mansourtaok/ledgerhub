package com.ledgerhub.model.dto.user;

import java.util.Set;

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
public class UserRequestDTO {

	private String email;
	private String password;
	private String abbreviation;
	private Long preferredLangId;
	private String preferredColor;
	private Set<Long> roleId;
}
