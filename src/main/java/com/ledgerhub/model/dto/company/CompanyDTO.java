package com.ledgerhub.model.dto.company;

import org.springframework.web.multipart.MultipartFile;

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
public class CompanyDTO {

	private Long id;
	private String name;
	private String email;
	private String phone;
	private String address;
	private String taxNumber;
	private String header;
	private String footer;
	private Long countryId;
	private Boolean active;
	private String website;
	private MultipartFile image;
	private String document;
}
