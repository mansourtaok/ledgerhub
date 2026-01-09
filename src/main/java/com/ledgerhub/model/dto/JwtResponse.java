package com.ledgerhub.model.dto;

import java.io.Serializable;

import com.ledgerhub.model.db.User;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public static JwtResponse from(User user, String token) {
		JwtResponse jwtResponse = new JwtResponse(token);
		jwtResponse.setEmail(user.getEmail());
		jwtResponse.setUserId(user.getId());
		return jwtResponse;
	}

}