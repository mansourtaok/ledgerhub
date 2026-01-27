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

import com.ledgerhub.model.dto.user.UserRequestDTO;
import com.ledgerhub.model.dto.user.UserResponseDTO;
import com.ledgerhub.service.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller")
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

	@PutMapping("/{id}")
	public UserResponseDTO update(@PathVariable("id") Long id, @RequestBody UserRequestDTO dto) {
		return userService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		userService.delete(id);
	}

	@DeleteMapping("/{id}/hard")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void hardDelete(@PathVariable("id") Long id) {
		userService.hardDelete(id);
	}
}
