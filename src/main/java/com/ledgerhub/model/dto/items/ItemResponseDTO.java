package com.ledgerhub.model.dto.items;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDTO {

	private Long id;
	private String name;
	private String sku;
	private Long categoryId;
	private BigDecimal sellingPrice;
	private BigDecimal stockQuantity;
	private Boolean status;
	private List<String> documents;
}
