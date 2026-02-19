package com.ledgerhub.model.db.items;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items", uniqueConstraints = { @UniqueConstraint(name = "UQ_items_sku", columnNames = "sku") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "company_id", nullable = false)
	private Integer companyId;

	@Column(name = "category_id")
	private Integer categoryId;

	@Column(name = "currency_id")
	private Integer currencyId;

	@Column(nullable = false, length = 150)
	private String name;

	@Column(nullable = false, length = 50)
	private String sku;

	@Column(columnDefinition = "TEXT")
	private String description;

	private String unit;

	private Boolean returnableStock;

	private BigDecimal stockQtyNotify;

	private String dimension;

	private String dimensionMeasure;

	private BigDecimal weight;

	private String packaging;

	private String manufacturer;

	private String brand;

	@Column(nullable = false)
	private BigDecimal costPrice;

	private String purchaseAccount;

	@Column(nullable = false)
	private BigDecimal sellingPrice;

	private String sellingAccount;

	@Column(columnDefinition = "TEXT")
	private String notes;

	private Boolean taxable;

	private BigDecimal taxPercent;

	@Column(nullable = false)
	private BigDecimal stockQuantity;

	private Boolean haveExpiryDate;

	private String lotNumber;

	private LocalDate expiryDate;

	private Boolean onlineSale;

	private Boolean status;

	private LocalDateTime createdAt;

	private Integer createdUserid;

	private LocalDateTime lastUpdateAt;

	private Integer lastUpdateUserid;
}
