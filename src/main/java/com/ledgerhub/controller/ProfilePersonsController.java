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

import com.ledgerhub.model.dto.ProfilePersonDTO;
import com.ledgerhub.service.profilePerson.IProfilePersonService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profiles/{companyId}/banks")
@Tag(name = "Profile Persons Controller")
@RequiredArgsConstructor
public class ProfilePersonsController {

	private final IProfilePersonService profilePersonsService;

	@PostMapping
	public ResponseEntity<ProfilePersonDTO> create(@PathVariable Long companyId, @RequestBody ProfilePersonDTO dto) {

		return ResponseEntity.ok(profilePersonsService.create(companyId, dto));
	}

	@GetMapping
	public ResponseEntity<List<ProfilePersonDTO>> list(@PathVariable Long companyId) {

		return ResponseEntity.ok(profilePersonsService.getByCompany(companyId));
	}

	@PutMapping("/{profilePersonId}")
	public ResponseEntity<ProfilePersonDTO> update(@PathVariable Long companyId, @PathVariable Long profilePersonId,
			@RequestBody ProfilePersonDTO dto) {

		return ResponseEntity.ok(profilePersonsService.update(companyId, profilePersonId, dto));
	}

	@DeleteMapping("/{profilePersonId}")
	public ResponseEntity<Void> delete(@PathVariable Long companyId, @PathVariable Long profilePersonId) {

		profilePersonsService.delete(companyId, profilePersonId);
		return ResponseEntity.noContent().build();
	}
}
