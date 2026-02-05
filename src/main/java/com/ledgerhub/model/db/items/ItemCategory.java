package com.ledgerhub.model.db.items;

import java.time.LocalDateTime;

import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@Column(nullable = false, length = 150)
	private String label;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_userid")
	private User createdUser;

	@Column(name = "last_update_at")
	private LocalDateTime lastUpdateAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_update_userid")
	private User lastUpdateUser;
}
