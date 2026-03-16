package com.ledgerhub.model.dto.expenses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExpenseRequestDTO {

	private Integer expTypeId;
	private LocalDateTime date;
	private BigDecimal amount;
	private String currency;
	private String paymentMethod;
	private String notes;
}
