package com.ledgerhub.model.db;

import java.time.LocalDate;
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
@Table(name = "staffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/* Relations */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id")
	private SystemLookup gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_description_id", nullable = false)
	private SystemLookup jobDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_userid")
	private User createdUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_update_userid")
	private User lastUpdateUser;

	/* Columns */

	@Column(name = "full_name", nullable = false, length = 150)
	private String fullName;

	@Column(columnDefinition = "TEXT")
	private String address;

	@Column(name = "contact_number", length = 50)
	private String contactNumber;

	@Column(length = 150)
	private String email;

	@Column(name = "join_date")
	private LocalDate joinDate;

	@Column(name = "leave_date")
	private LocalDate leaveDate;

	@Column(name = "have_cnss")
	private Boolean haveCnss;

	@Column(name = "cnss_number", length = 50)
	private String cnssNumber;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "last_update_at")
	private LocalDateTime lastUpdateAt;

	@Column
	private Boolean active;

	@PrePersist
	void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.active = Boolean.TRUE;
		this.haveCnss = Boolean.FALSE;
	}

	@PreUpdate
	void onUpdate() {
		this.lastUpdateAt = LocalDateTime.now();
	}
}
