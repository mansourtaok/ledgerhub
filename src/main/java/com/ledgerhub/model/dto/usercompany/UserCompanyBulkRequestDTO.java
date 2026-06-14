package com.ledgerhub.model.dto.usercompany;

import java.util.List;

import lombok.Data;

@Data
public class UserCompanyBulkRequestDTO {
	private Long companyId;
	private List<UserEntry> users;

	@Data
	public static class UserEntry {
		private Long userId;
		private Boolean isDefault;
	}
}
