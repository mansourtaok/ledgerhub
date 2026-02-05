package com.ledgerhub.service.items.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.items.ItemCategory;
import com.ledgerhub.model.dto.company.CompanyDTO;
import com.ledgerhub.model.dto.items.ItemCategoryRequestDTO;
import com.ledgerhub.model.dto.items.ItemCategoryResponseDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.ItemCategoryRepository;
import com.ledgerhub.service.items.IItemCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemCategoryService implements IItemCategoryService {

	private final ItemCategoryRepository repository;
	private final CompanyRepository companyRepository;

	@Override
	public ItemCategoryResponseDTO create(ItemCategoryRequestDTO dto) {

		Company company = companyRepository.findById(dto.getCompanyId())
				.orElseThrow(() -> new IllegalArgumentException("Company not found"));

		ItemCategory category = ItemCategory.builder().company(company).label(dto.getLabel())
				.parentId(dto.getParentId()).build();

		return toDto(repository.save(category));
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new IllegalArgumentException("Category not found");
		}
		repository.deleteById(id);
	}

	@Override
	public ItemCategoryResponseDTO update(Long id, ItemCategoryRequestDTO dto) {

		ItemCategory category = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Category not found"));

		Company company = companyRepository.findById(dto.getCompanyId())
				.orElseThrow(() -> new IllegalArgumentException("Company not found"));

		category.setCompany(company);
		category.setLabel(dto.getLabel());
		category.setParentId(dto.getParentId());
		category.setLastUpdateAt(LocalDateTime.now());

		return toDto(repository.save(category));
	}

	@Override
	public Page<ItemCategoryResponseDTO> findAll(Long companyId, String label, Integer parentId, Pageable pageable) {

		return repository.search(companyId, label, parentId, pageable).map(this::toDto);
	}

	private ItemCategoryResponseDTO toDto(ItemCategory c) {

		ItemCategoryResponseDTO parentDTO = null;
		if (c.getParentId() != null && c.getParentId().intValue() > 0) {
			Optional<ItemCategory> parent = this.repository.findById(c.getParentId().longValue());
			if (parent.isPresent()) {
				ItemCategory p = parent.get();
				parentDTO = ItemCategoryResponseDTO.builder().id(p.getId()).label(p.getLabel())
						.createdAt(p.getCreatedAt()).build();
			}
		}

		return ItemCategoryResponseDTO.builder().id(c.getId()).label(c.getLabel()).parent(parentDTO)
				.createdAt(c.getCreatedAt()).build();
	}
}
