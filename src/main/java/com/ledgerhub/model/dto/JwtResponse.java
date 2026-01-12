package com.ledgerhub.model.dto;

import java.io.Serializable;

import com.ledgerhub.model.db.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	private final String jwttoken;
	private String firstName;
	private String lastName;
	private String email;
	private Long userId;
	private Boolean isAdmin;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public static JwtResponse from(User user, String token) {
		JwtResponse jwtResponse = new JwtResponse(token);
		jwtResponse.setEmail(user.getEmail());
		jwtResponse.setUserId(user.getId());
		return jwtResponse;
	}

}