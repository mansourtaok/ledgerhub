package com.ledgerhub.model.dto.items;

import java.time.LocalDateTime;

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
public class ItemCategoryResponseDTO {

	private Long id;
	private String label;
	private ItemCategoryResponseDTO parent;
	private LocalDateTime createdAt;
}
