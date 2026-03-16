package com.ledgerhub.model.dto.warehouse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WarehouseRequestDTO {

	private String name;
	private String mainContact;
	private String email;
	private String contactNumber;
	private String address;
	private Boolean active;
}
