package com.ledgerhub.model.db;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long companyId;

	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(name = "main_contact", nullable = false, length = 150)
    private String mainContact;

	@Column(length = 150)
    private String email;

    @Column(name = "contact_number", length = 50)
    private String contactNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column
    private Boolean active = true;

    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

	@PrePersist
	void prePersist() {
		this.createdOn = LocalDateTime.now();
		this.active = Boolean.TRUE;
	}
}
