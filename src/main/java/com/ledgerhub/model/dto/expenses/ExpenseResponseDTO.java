package com.ledgerhub.model.dto.expenses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseResponseDTO {

	private Long id;
	private Integer expTypeId;
	private LocalDateTime date;
	private BigDecimal amount;
	private String currencyId;
	private String payment_method;
	private String notes;
}
