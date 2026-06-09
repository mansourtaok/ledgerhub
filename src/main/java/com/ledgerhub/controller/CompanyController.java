package com.ledgerhub.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.company.CompanyDTO;
import com.ledgerhub.service.impl.CompanyService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company Controller")
public class CompanyController {

	private final CompanyService companyService;

	@PostMapping(consumes = { "multipart/form-data", "application/json" })
	public ResponseEntity<CompanyDTO> create(@ModelAttribute CompanyDTO dto) {
		return ResponseEntity.ok(companyService.create(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(companyService.getById(id));
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAll() {
		List<CompanyDTO> companies = companyService.getAll();
		return ResponseEntity.ok(Map.of("totalData", companies.size(), "data", companies));
	}

	@PutMapping(value = "/{id}", consumes = { "multipart/form-data", "application/json" })
	public ResponseEntity<CompanyDTO> update(@PathVariable("id") Long id, @ModelAttribute CompanyDTO dto) {
		return ResponseEntity.ok(companyService.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deactivate(@PathVariable Long id) {
		companyService.deactivate(id);
		return ResponseEntity.noContent().build();
	}
}
