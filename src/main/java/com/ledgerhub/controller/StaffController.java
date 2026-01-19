package com.ledgerhub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.ledgerhub.model.dto.staff.StaffDTO;
import com.ledgerhub.service.staff.IStaffExcelService;
import com.ledgerhub.service.staff.IStaffService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StaffController {

	private final IStaffService staffService;
	private final IStaffExcelService staffExcelServicee;

	@PostMapping("/api/staffs")
	public ResponseEntity<StaffDTO> create(@RequestBody StaffDTO dto, @RequestParam("userId") Long userId) {
		return ResponseEntity.ok(staffService.create(dto, userId));
	}

	@GetMapping("/api/staffs/{id}")
	public ResponseEntity<StaffDTO> getById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(staffService.getById(id));
	}

	@GetMapping("/api/staffs")
	public ResponseEntity<Page<StaffDTO>> getAll(@RequestParam(required = false, name = "fullName") String fullName,
			@RequestParam(required = false, name = "jobDescriptionId") Long jobDescriptionId,
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

		return ResponseEntity.ok(staffService.getAll(fullName, jobDescriptionId, pageable));
	}

	@PutMapping("/api/staffs/{id}")
	public ResponseEntity<StaffDTO> update(@PathVariable("id") Long id, @RequestBody StaffDTO dto,
			@RequestParam Long userId) {
		return ResponseEntity.ok(staffService.update(id, dto, userId));
	}

	@DeleteMapping("/api/staffs/{id}")
	public ResponseEntity<Void> deactivate(@PathVariable Long id) {
		staffService.deactivate(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/api/companies/{companyId}/staffs")
	public ResponseEntity<?> importStaffs(@PathVariable("companyId") Long companyId,
			@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
		staffExcelServicee.importFromExcel(companyId, userId, userId, file);
		return ResponseEntity.ok("Staff imported successfully");
	}
}
