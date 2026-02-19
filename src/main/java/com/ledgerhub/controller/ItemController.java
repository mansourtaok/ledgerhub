package com.ledgerhub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.items.ItemRequestDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;
import com.ledgerhub.service.items.impl.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping("/api/items")
	public ItemResponseDTO create(@RequestBody ItemRequestDTO dto) {
		return itemService.create(dto);
	}

	@GetMapping("/api/items/{id}")
	public ItemResponseDTO getById(@PathVariable Long id) {
		return itemService.getById(id);
	}

	@GetMapping("/api/companies/{companyId}/items")
	public Page<ItemResponseDTO> getAll(@PathVariable("companyId") Long companyId,
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "categoryIds") List<Integer> categoryIds,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size,
			@RequestParam(name = "sort", required = false) String[] sort) {
		return itemService.getAll(name, categoryIds, page, size, sort);
	}

	@PutMapping("/api/items/{id}")
	public ItemResponseDTO update(@PathVariable Long id, @RequestBody ItemRequestDTO dto) {
		return itemService.update(id, dto);
	}

	@DeleteMapping("/api/items/{id}")
	public void delete(@PathVariable Long id) {
		itemService.delete(id);
	}
}
