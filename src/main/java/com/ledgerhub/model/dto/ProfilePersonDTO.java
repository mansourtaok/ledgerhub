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
public class ProfilePersonDTO {

	private Long id;
	private Long companyId;
	private Long profileId;

	private String name;
	private Long jobDescriptionId;
	private String email;
	private String contactNumber;
	private String extension;

}
