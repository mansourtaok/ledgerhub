package com.ledgerhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.dto.warehouse.WarehouseRequestDTO;
import com.ledgerhub.model.dto.warehouse.WarehouseResponseDTO;
import com.ledgerhub.service.warehouse.IWarehouseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WarehouseController {

	private final IWarehouseService warehouseService;

	@PostMapping("/api/warehouses/{companyId}")
	@ResponseStatus(HttpStatus.CREATED)
	public WarehouseResponseDTO create(@RequestBody WarehouseRequestDTO dto, @PathVariable("companyId") Long companyId) {
		return warehouseService.create(dto, companyId);
	}

	@PutMapping("/api/warehouses/{id}")
	public WarehouseResponseDTO update(@PathVariable("id") Long id, @RequestBody WarehouseRequestDTO dto) {
		return warehouseService.update(id, dto);
	}

	@DeleteMapping("/api/warehouses/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		warehouseService.delete(id);
	}
	
	@GetMapping("/api/warehouses/{companyId}")
	public ResponseEntity<List<WarehouseResponseDTO>> list(@PathVariable("companyId") Long companyId) {

	    List<WarehouseResponseDTO> list = warehouseService.findAll(companyId);
	    return ResponseEntity.ok(list);
	}

}
