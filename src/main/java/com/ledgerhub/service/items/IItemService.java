package com.ledgerhub.service.items;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.dto.items.ItemRequestDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface IItemService {

	ItemResponseDTO create(ItemRequestDTO dto);

	Page<ItemResponseDTO> getAll(String name, List<Integer> categoryIds, int page, int size, String[] sort);

	ItemResponseDTO getById(Long id);

	ItemResponseDTO update(Long id, ItemRequestDTO dto);

	void delete(Long id);

	void importFromExcel(Long companyId, MultipartFile file, HttpServletRequest request);
}
