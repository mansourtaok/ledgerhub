package com.ledgerhub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.dto.profile.ProfileDTO;
import com.ledgerhub.service.profile.IProfileExcelService;
import com.ledgerhub.service.profile.IProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Profile Controller")
public class ProfileController {

	private final IProfileService profileService;
	//private final IProfileExcelService profileExcelServicee;

	@PostMapping("/api/profiles")
	public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto, @RequestParam("userId") Long userId) {
		return ResponseEntity.ok(profileService.create(dto, userId));
	}

	@GetMapping("/api/profiles/{id}")
	public ResponseEntity<ProfileDTO> getById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(profileService.getById(id));
	}

	@GetMapping("/api/profiles")
	public ResponseEntity<Page<ProfileDTO>> getAll(@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "categoryId") Long categoryId,
			@RequestParam(required = false, name = "typeId") Long typeId,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size,
			@RequestParam(name = "sort", required = false) String[] sort) {

		// If no sort is provided, set default manually
		if (sort == null || sort.length == 0) {
			sort = new String[] { "createdAt,desc" };
		}

		// Convert sort strings into Sort.Order objects
		List<Sort.Order> orders = new ArrayList<>();
		for (String s : sort) {
			String[] parts = s.split(",", 2); // split into [property, direction]
			String property = parts[0].trim();
			Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC
					: Sort.Direction.ASC;
			orders.add(new Sort.Order(direction, property));
		}

		Sort sorting = Sort.by(orders);

		Pageable pageable = PageRequest.of(page, size, sorting);

		return ResponseEntity.ok(profileService.getAll(name, categoryId, typeId, pageable));
	}

	@PutMapping("/api/profiles/{id}")
	public ResponseEntity<ProfileDTO> update(@PathVariable("id") Long id, @RequestBody ProfileDTO dto,
			@RequestParam Long userId) {
		return ResponseEntity.ok(profileService.update(id, dto, userId));
	}
/*
	@PostMapping("/api/companies/{companyId}/profiles")
	public ResponseEntity<?> importProfiles(@PathVariable("companyId") Long companyId,
			@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
		profileExcelServicee.importFromExcel(companyId, userId, userId, file);
		return ResponseEntity.ok("Profile imported successfully");
	}

	@GetMapping("/api/companies/{companyId}/profiles/export")
	public ResponseEntity<byte[]> exportProfiles(@PathVariable("companyId") Long companyId) {

		byte[] file = profileExcelServicee.exportProfilesByCompany(companyId);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=profiles-company-" + companyId + ".xlsx")
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(file);
	}*/
}
