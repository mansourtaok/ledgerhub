package com.ledgerhub.model.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTypeRequestDTO {
	private String expTypeLabel;
	private Boolean active;
}
