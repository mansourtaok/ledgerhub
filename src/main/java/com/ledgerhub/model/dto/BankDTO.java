package com.ledgerhub.model.dto;

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
public class BankDTO {

	private Long id;
	private Long companyId;

	private String bankName;
	private String bankAccNbr;
	private String bankAccLabel;
	private String finAccNbr;
	private String swiftNbr;
	private String ibanNbr;

	private Long currencyId;
	private Long countryId;
}
