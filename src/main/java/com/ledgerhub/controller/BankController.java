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
import com.ledgerhub.service.bank.IBankService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies/{companyId}/banks")
@Tag(name = "Company Banks Controller")
@RequiredArgsConstructor
public class BankController {

	private final IBankService bankService;

	@PostMapping
	public ResponseEntity<BankDTO> create(@PathVariable Long companyId, @RequestBody BankDTO dto) {

		return ResponseEntity.ok(bankService.create(companyId, dto));
	}

	@GetMapping
	public ResponseEntity<List<BankDTO>> list(@PathVariable Long companyId) {

		return ResponseEntity.ok(bankService.getByCompany(companyId));
	}

	@PutMapping("/{bankId}")
	public ResponseEntity<BankDTO> update(@PathVariable Long companyId, @PathVariable Long bankId,
			@RequestBody BankDTO dto) {

		return ResponseEntity.ok(bankService.update(companyId, bankId, dto));
	}

	@DeleteMapping("/{bankId}")
	public ResponseEntity<Void> delete(@PathVariable Long companyId, @PathVariable Long bankId) {

		bankService.delete(companyId, bankId);
		return ResponseEntity.noContent().build();
	}
}
