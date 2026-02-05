package com.ledgerhub.model.dto.items;

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
public class ItemCategoryRequestDTO {

	private Long companyId;
	private String label;
	private Integer parentId;
}
