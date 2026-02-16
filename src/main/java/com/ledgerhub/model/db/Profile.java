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
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private SystemLookup categoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable = false)
	private SystemLookup typeId;

	@Column(nullable = false, length = 150)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "default_currency", nullable = false)
	private SystemLookup currencyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_term", nullable = false)
	private SystemLookup paymentTerm;

	@Column(name = "country_id", nullable = false)
	private Long countryId;

	@Column(columnDefinition = "TEXT")
	private String address;

	@Column(length = 50)
	private String contactNumber;

	@Column(length = 50)
	private String faxNumber;

	@Column(length = 50)
	private String vendorAccount;

	@Column(length = 50)
	private String customerAccount;

	@Column(length = 50)
	private String taxNumber;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "last_update_at")
	private LocalDateTime lastUpdateAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_userid")
	private User createdUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_update_userid")
	private User lastUpdateUser;

	@PrePersist
	void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.lastUpdateAt = LocalDateTime.now();
	}

	@PreUpdate
	void onUpdate() {
		this.lastUpdateAt = LocalDateTime.now();
	}
}
