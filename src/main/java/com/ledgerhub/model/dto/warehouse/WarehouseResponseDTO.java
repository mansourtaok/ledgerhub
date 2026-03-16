package com.ledgerhub.model.dto.warehouse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseResponseDTO {

	private Long id;
	private String name;
	private String mainContact;
	private String email;
	private String contactNumber;
	private String address;
	private Boolean active;
}
