package com.ledgerhub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.BankDTO;
import com.ledgerhub.service.bank.impl.BankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

	private final BankService bankService;

	@PostMapping
	public ResponseEntity<BankDTO> create(@RequestBody BankDTO dto) {
		return ResponseEntity.ok(bankService.create(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<BankDTO> update(@PathVariable("id") Long id, @RequestBody BankDTO dto) {
		return ResponseEntity.ok(bankService.update(id, dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BankDTO> getById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(bankService.getById(id));
	}

	@GetMapping("/company/{companyId}")
	public ResponseEntity<List<BankDTO>> getByCompany(@PathVariable("companyId") Long companyId) {
		return ResponseEntity.ok(bankService.getByCompany(companyId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		bankService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
