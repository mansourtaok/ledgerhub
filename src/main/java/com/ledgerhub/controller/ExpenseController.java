package com.ledgerhub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import com.ledgerhub.model.dto.expenses.ExpenseRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseResponseDTO;
import com.ledgerhub.service.expenses.IExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

	private final IExpenseService expService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseResponseDTO create(@RequestBody ExpenseRequestDTO dto,
			@RequestHeader("X-Company-Id") Long companyId) {
		return expService.create(dto, companyId);
	}

	@PutMapping("/{id}")
	public ExpenseResponseDTO update(@PathVariable("id") Long id, @RequestBody ExpenseRequestDTO dto) {
		return expService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		expService.delete(id);
	}

	@GetMapping
	public Page<ExpenseResponseDTO> getAll(@RequestHeader("X-Company-Id") Long companyId,
			@RequestParam(required = false, name = "notes") String notes,
			@RequestParam(required = false, name = "expTypeIds") List<Integer> expTypeIds,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size,
			@RequestParam(name = "sort", required = false) String[] sort) {
		return expService.getAll(notes, expTypeIds, page, size, sort);
	}
}
