package com.ledgerhub.model.dto.items;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ItemRequestDTO {

	private Integer companyId;
	private Integer categoryId;
	private Integer currencyId;
	private String name;
	private String sku;
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
	private BigDecimal costPrice;
	private String purchaseAccount;
	private BigDecimal sellingPrice;
	private String sellingAccount;
	private String notes;
	private Boolean taxable;
	private BigDecimal taxPercent;
	private Boolean haveExpiryDate;
	private String lotNumber;
	private LocalDate expiryDate;
	private Boolean onlineSale;
	private Boolean status;
	private BigDecimal stockQuantity;
}
