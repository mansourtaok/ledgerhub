package com.ledgerhub.model.dto.company;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.dto.country.CountryDTO;

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
	private CountryDTO country;
	private Boolean active;
	private Boolean isDefault;
	private String website;
	private MultipartFile image;
	private String imageUrl;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String createdUser;
	private String updatedUser;
}
