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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.expenses.ExpenseTypeRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseTypeResponseDTO;
import com.ledgerhub.service.expenses.IExpenseTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpenseTypeController {

	private final IExpenseTypeService expTypService;

	@PostMapping("/api/expense-types/{companyId}")
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseTypeResponseDTO create(@RequestBody ExpenseTypeRequestDTO dto, @PathVariable("companyId") Long companyId) {
		return expTypService.create(dto, companyId);
	}

	@PutMapping("/api/expense-types/{id}")
	public ExpenseTypeResponseDTO update(@PathVariable("id") Long id, @RequestBody ExpenseTypeRequestDTO dto) {
		return expTypService.update(id, dto);
	}

	@DeleteMapping("/api/expense-types/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		expTypService.delete(id);
	}
	
	@GetMapping("/api/expense-types/{companyId}")
	public ResponseEntity<List<ExpenseTypeResponseDTO>> list(@PathVariable("companyId") Long companyId) {

	    List<ExpenseTypeResponseDTO> list = expTypService.findAll(companyId);
	    return ResponseEntity.ok(list);
	}

}
