package com.ledgerhub.service.items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.dto.items.ItemCategoryRequestDTO;
import com.ledgerhub.model.dto.items.ItemCategoryResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface IItemCategoryService {

	Page<ItemCategoryResponseDTO> findAll(Long companyId, String label, Integer parentId, Pageable pageable);

	ItemCategoryResponseDTO create(ItemCategoryRequestDTO dto);

	ItemCategoryResponseDTO update(Long id, ItemCategoryRequestDTO dto);

	void delete(Long id);

	void importFromExcel(Long companyId, MultipartFile file, HttpServletRequest request);

}
