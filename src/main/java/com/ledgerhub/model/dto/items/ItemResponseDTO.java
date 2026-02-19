package com.ledgerhub.model.dto.items;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDTO {

	private Long id;
	private String name;
	private String sku;
	private Integer categoryId;
	private BigDecimal sellingPrice;
	private BigDecimal stockQuantity;
	private Boolean status;
}
