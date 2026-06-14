package com.ledgerhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.usercompany.UserCompanyBulkRequestDTO;
import com.ledgerhub.model.dto.usercompany.UserCompanyRequestDTO;
import com.ledgerhub.model.dto.usercompany.UserCompanyResponseDTO;
import com.ledgerhub.service.usercompany.IUserCompanyService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-companies")
@Tag(name = "User Company Controller")
@RequiredArgsConstructor
public class UserCompanyController {

	private final IUserCompanyService userCompanyService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserCompanyResponseDTO assign(@RequestBody UserCompanyRequestDTO dto) {
		return userCompanyService.assign(dto);
	}

	@PostMapping("/bulk")
	@ResponseStatus(HttpStatus.CREATED)
	public List<UserCompanyResponseDTO> assignBulk(@RequestBody UserCompanyBulkRequestDTO dto) {
		return userCompanyService.assignBulk(dto);
	}

	@GetMapping("/user/{userId}")
	public List<UserCompanyResponseDTO> getByUser(@PathVariable Long userId) {
		return userCompanyService.getByUser(userId);
	}

	@PutMapping("/{id}/default")
	public UserCompanyResponseDTO setDefault(@PathVariable Long id) {
		return userCompanyService.setDefault(id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long id) {
		userCompanyService.remove(id);
	}
}
