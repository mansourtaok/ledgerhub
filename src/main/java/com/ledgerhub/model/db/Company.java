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
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	private String name;

	@Column(length = 150)
	private String email;

	@Column(length = 50)
	private String phone;

	@Column(columnDefinition = "TEXT")
	private String address;

	@Column(name = "tax_number", length = 50)
	private String taxNumber;

	@Column(length = 200)
	private String header;

	@Column(length = 200)
	private String footer;

	@Column(length = 255)
	private String website;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_userid")
	private User createdUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_userid")
	private User updatedUser;

	@Column
	private Boolean active;

	@PrePersist
	void prePersist() {
		this.createdDate = LocalDateTime.now();
		this.updatedDate = this.createdDate;
		this.active = Boolean.TRUE;
	}

	@PreUpdate
	void preUpdate() {
		this.updatedDate = LocalDateTime.now();
	}
}
