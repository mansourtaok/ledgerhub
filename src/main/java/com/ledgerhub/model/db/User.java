package com.ledgerhub.model.db;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = User.TABLE_NAME)
@Getter
@Setter
public class User {

	public static final String TABLE_NAME = "users";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "password_hash")
	private String password;

	@Column(length = 3)
	private String abbreviation;

	@Column(name = "preferred_color", length = 7)
	private String preferredColor;

	@Column(name = "preferred_lang")
	private String preferredLang;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	private Boolean active = true;

	@Column(name = "role_id")
	private Long roleId;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

}
