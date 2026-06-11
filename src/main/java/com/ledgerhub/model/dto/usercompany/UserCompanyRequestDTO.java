package com.ledgerhub.model.dto.usercompany;

import lombok.Data;

@Data
public class UserCompanyRequestDTO {
	private Long userId;
	private Long companyId;
	private Boolean isDefault;
}
