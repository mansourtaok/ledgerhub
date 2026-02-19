package com.ledgerhub.service.items;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ledgerhub.model.dto.items.ItemRequestDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;

public interface IItemService {

	ItemResponseDTO create(ItemRequestDTO dto);

	Page<ItemResponseDTO> getAll(String name, List<Integer> categoryIds, int page, int size, String[] sort);

	ItemResponseDTO getById(Long id);

	ItemResponseDTO update(Long id, ItemRequestDTO dto);

	void delete(Long id);

}
