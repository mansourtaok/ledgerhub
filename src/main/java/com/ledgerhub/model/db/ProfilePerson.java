package com.ledgerhub.model.db;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePerson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "company_id", nullable = false)
	private Long companyId;

	@Column(name = "profile_id", length = 50)
	private Long profileId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_description_id", nullable = false)
	private SystemLookup jobDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_userid")
	private User createdUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_update_userid")
	private User lastUpdateUser;

	@Column(name = "name", nullable = false, length = 150)
	private String name;

	@Column(name = "email", length = 150)
	private String email;

	@Column(name = "contact_number", length = 50)
	private String contactNumber;

	@Column(name = "extension", length = 10)
	private String extension;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
