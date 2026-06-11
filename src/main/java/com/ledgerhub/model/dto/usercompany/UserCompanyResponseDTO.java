package com.ledgerhub.model.dto.usercompany;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCompanyResponseDTO {
	private Long id;
	private Long userId;
	private String userEmail;
	private Long companyId;
	private String companyName;
	private Boolean isDefault;
	private LocalDateTime createdAt;
}
