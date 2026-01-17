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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column
	private Boolean active;

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.active = Boolean.TRUE;
	}
}
