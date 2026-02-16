package com.ledgerhub.model.dto.profile;

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
public class ProfileDTO {

	private Long id;
	private Long companyId;
	private Long categoryId;
	private Long typeId;
	private String name;
	private Long countryId;
	private Long currencyId;
	private Long paymentTerm;
	private String address;
	private String contactNumber;
	private String faxNumber;
	private String vendorAccountNumber;
	private String customerAccountNumber;
	private String taxNumber;
}
