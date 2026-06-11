package com.ledgerhub.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.expenses.ExpenseTypeRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseTypeResponseDTO;
import com.ledgerhub.service.expenses.IExpenseTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expense-types")
@RequiredArgsConstructor
public class ExpenseTypeController {

	private final IExpenseTypeService expTypService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseTypeResponseDTO create(@RequestBody ExpenseTypeRequestDTO dto,
			@RequestHeader("X-Company-Id") Long companyId) {
		return expTypService.create(dto, companyId);
	}

	@PutMapping("/{id}")
	public ExpenseTypeResponseDTO update(@PathVariable("id") Long id, @RequestBody ExpenseTypeRequestDTO dto) {
		return expTypService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		expTypService.delete(id);
	}

	@GetMapping
	public ResponseEntity<List<ExpenseTypeResponseDTO>> list(@RequestHeader("X-Company-Id") Long companyId) {
		return ResponseEntity.ok(expTypService.findAll(companyId));
	}
}
