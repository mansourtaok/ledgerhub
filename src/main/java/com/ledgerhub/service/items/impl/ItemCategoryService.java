package com.ledgerhub.service.items.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.config.jwt.JwtTokenUtil;
import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.User;
import com.ledgerhub.model.db.items.ItemCategory;
import com.ledgerhub.model.dto.items.ItemCategoryRequestDTO;
import com.ledgerhub.model.dto.items.ItemCategoryResponseDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.ItemCategoryRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.items.IItemCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemCategoryService implements IItemCategoryService {

	private final ItemCategoryRepository repository;
	private final CompanyRepository companyRepository;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

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

	@Transactional
	@Override
	public void importFromExcel(Long companyId, MultipartFile file, HttpServletRequest request) {

		try {

			Long userId = jwtTokenUtil.getUserIdFromToken(request.getHeader("Authorization"));

			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

			Company company = companyRepository.findById(companyId)
					.orElseThrow(() -> new RuntimeException("Company not found"));

			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				String label = row.getCell(0).getStringCellValue().trim();

				Integer parentId = null;
				if (row.getCell(1) != null) {
					String parentLabel = row.getCell(1).getStringCellValue().trim();

					if (parentLabel != null && !parentLabel.isEmpty()) {
						ItemCategory parent = repository.findByCompanyIdAndLabel(companyId, parentLabel)
								.orElseThrow(() -> new RuntimeException("Parent category not found: " + parentLabel));

						parentId = parent.getId().intValue();
					}

				}
				ItemCategory category = ItemCategory.builder().company(company).label(label).parentId(parentId)
						.createdAt(LocalDateTime.now()).createdUser(user).lastUpdateAt(LocalDateTime.now())
						.lastUpdateUser(user).build();

				repository.save(category);

			}

			workbook.close();

		} catch (Exception e) {
			throw new RuntimeException("Error importing categories: " + e.getMessage());
		}
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
