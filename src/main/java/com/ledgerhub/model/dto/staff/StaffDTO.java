package com.ledgerhub.model.dto.staff;

import java.time.LocalDate;

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
public class StaffDTO {

	private Long id;
	private Long companyId;
	private String fullName;
	private Long genderId;
	private Long jobDescriptionId;
	private String address;
	private String contactNumber;
	private String email;
	private LocalDate joinDate;
	private LocalDate leaveDate;
	private Boolean haveCnss;
	private String cnssNumber;
	private String notes;
	private Boolean active;
}
