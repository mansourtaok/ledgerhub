package com.ledgerhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.user.UserRequestDTO;
import com.ledgerhub.model.dto.user.UserResponseDTO;
import com.ledgerhub.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDTO create(@RequestBody UserRequestDTO dto) {
		return userService.create(dto);
	}

	@GetMapping("/{id}")
	public UserResponseDTO getById(@PathVariable("id") Long id) {
		return userService.getById(id);
	}

	@GetMapping
	public List<UserResponseDTO> getAll() {
		return userService.getAll();
	}

	// SOFT DELETE
	@PatchMapping("/{id}/deactivate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deactivate(@PathVariable Long id) {
		// userService.deactivate(id);
	}

	// HARD DELETE
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		// userService.delete(id);
	}
}
