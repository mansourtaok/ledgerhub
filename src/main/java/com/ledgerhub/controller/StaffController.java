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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.dto.staff.StaffDTO;
import com.ledgerhub.service.staff.IStaffExcelService;
import com.ledgerhub.service.staff.IStaffService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
@Tag(name = "Staff Controller")
public class StaffController {

	private final IStaffService staffService;
	private final IStaffExcelService staffExcelServicee;

	@PostMapping
	public ResponseEntity<StaffDTO> create(@RequestBody StaffDTO dto, @RequestParam("userId") Long userId) {
		return ResponseEntity.ok(staffService.create(dto, userId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<StaffDTO> getById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(staffService.getById(id));
	}

	@GetMapping
	public ResponseEntity<Page<StaffDTO>> getAll(@RequestParam(required = false, name = "fullName") String fullName,
			@RequestParam(required = false, name = "jobDescriptionId") Long jobDescriptionId,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size,
			@RequestParam(name = "sort", required = false) String[] sort) {

		if (sort == null || sort.length == 0) {
			sort = new String[] { "createdAt,desc" };
		}

		List<Sort.Order> orders = new ArrayList<>();
		for (String s : sort) {
			String[] parts = s.split(",", 2);
			String property = parts[0].trim();
			Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC
					: Sort.Direction.ASC;
			orders.add(new Sort.Order(direction, property));
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
		return ResponseEntity.ok(staffService.getAll(fullName, jobDescriptionId, pageable));
	}

	@PutMapping("/{id}")
	public ResponseEntity<StaffDTO> update(@PathVariable("id") Long id, @RequestBody StaffDTO dto,
			@RequestParam Long userId) {
		return ResponseEntity.ok(staffService.update(id, dto, userId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deactivate(@PathVariable Long id) {
		staffService.deactivate(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/import")
	public ResponseEntity<?> importStaffs(@RequestHeader("X-Company-Id") Long companyId,
			@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
		staffExcelServicee.importFromExcel(companyId, userId, userId, file);
		return ResponseEntity.ok("Staff imported successfully");
	}

	@GetMapping("/export")
	public ResponseEntity<byte[]> exportStaffs(@RequestHeader("X-Company-Id") Long companyId) {
		byte[] file = staffExcelServicee.exportStaffsByCompany(companyId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=staffs-company-" + companyId + ".xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(file);
	}
}
