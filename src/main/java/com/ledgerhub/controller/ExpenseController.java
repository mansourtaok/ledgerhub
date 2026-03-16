package com.ledgerhub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.expenses.ExpenseRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseResponseDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;
import com.ledgerhub.service.expenses.IExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

	private final IExpenseService expService;

	@PostMapping("/api/expense/{companyId}")
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseResponseDTO create(@RequestBody ExpenseRequestDTO dto, @PathVariable("companyId") Long companyId) {
		return expService.create(dto, companyId);
	}

	@PutMapping("/api/expense/{id}")
	public ExpenseResponseDTO update(@PathVariable("id") Long id, @RequestBody ExpenseRequestDTO dto) {
		return expService.update(id, dto);
	}

	@DeleteMapping("/api/expense/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		expService.delete(id);
	}

	@GetMapping("/api/expense/{companyId}/expenses")
	public Page<ExpenseResponseDTO> getAll(@PathVariable("companyId") Long companyId,
				@RequestParam(required = false, name = "notes") String notes,
				@RequestParam(required = false, name = "expTypeIds") List<Integer> expTypeIds,
				@RequestParam(defaultValue = "0", name = "page") int page,
				@RequestParam(defaultValue = "10", name = "size") int size,
				@RequestParam(name = "sort", required = false) String[] sort) {
			return expService.getAll(notes, expTypeIds, page, size, sort);
	}
	
}
