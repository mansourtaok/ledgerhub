package com.ledgerhub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.dto.items.ItemCategoryRequestDTO;
import com.ledgerhub.model.dto.items.ItemCategoryResponseDTO;
import com.ledgerhub.service.items.IItemCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/item-categories")
@RequiredArgsConstructor
public class ItemCategoryController {

	private final IItemCategoryService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemCategoryResponseDTO create(@RequestBody ItemCategoryRequestDTO dto) {
		return service.create(dto);
	}

	@PutMapping("/{id}")
	public ItemCategoryResponseDTO update(@PathVariable("id") Long id, @RequestBody ItemCategoryRequestDTO dto) {
		return service.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@GetMapping
	public Page<ItemCategoryResponseDTO> getAll(@RequestHeader("X-Company-Id") Long companyId,
			@RequestParam(required = false, name = "label") String label,
			@RequestParam(required = false, name = "parentId") Integer parentId,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size,
			@RequestParam(name = "sort", required = false) String[] sort) {

		if (sort == null || sort.length == 0) {
			sort = new String[] { "label,desc" };
		}

		List<Sort.Order> orders = new ArrayList<>();
		for (String s : sort) {
			String[] parts = s.split(",", 2);
			String property = parts[0].trim();
			Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC
					: Sort.Direction.ASC;
			orders.add(new Sort.Order(direction, property));
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
		return service.findAll(companyId, label, parentId, pageable);
	}

	@PostMapping("/import")
	public ResponseEntity<String> importCategories(@RequestHeader("X-Company-Id") Long companyId,
			@RequestParam(name = "file") MultipartFile file, HttpServletRequest request) {
		service.importFromExcel(companyId, file, request);
		return ResponseEntity.ok("Categories imported successfully");
	}
}
