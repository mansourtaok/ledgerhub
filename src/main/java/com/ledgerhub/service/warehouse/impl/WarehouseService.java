package com.ledgerhub.service.warehouse.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.Warehouse;
import com.ledgerhub.model.dto.warehouse.WarehouseRequestDTO;
import com.ledgerhub.model.dto.warehouse.WarehouseResponseDTO;
import com.ledgerhub.repository.WarehouseRepository;
import com.ledgerhub.service.warehouse.IWarehouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService {

	private final WarehouseRepository repository;

	@Override
	public WarehouseResponseDTO create(WarehouseRequestDTO dto, Long companyId) {
		Warehouse warehouse = new Warehouse();
				
		warehouse.setCompanyId(companyId);
		warehouse.setName(dto.getName());
		warehouse.setMainContact(dto.getMainContact());
		warehouse.setEmail(dto.getEmail());
		warehouse.setContactNumber(dto.getContactNumber());
		warehouse.setAddress(dto.getAddress());
		warehouse.setActive(dto.getActive());
		return mapToDTO(repository.save(warehouse));
		
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new IllegalArgumentException("Warehouse not found");
		}//TODO Check usage of id
		//repository.deleteById(id);
		
		Warehouse warehouse = new Warehouse();
		warehouse.setActive(false);
		repository.save(warehouse);
	}

	@Override
	public WarehouseResponseDTO update(Long id, WarehouseRequestDTO dto) {

		Warehouse warehouse = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

		warehouse.setName(dto.getName());
		warehouse.setMainContact(dto.getMainContact());
		warehouse.setEmail(dto.getEmail());
		warehouse.setContactNumber(dto.getContactNumber());
		warehouse.setAddress(dto.getAddress());
		warehouse.setActive(dto.getActive());

		return mapToDTO(repository.save(warehouse));
	}

	@Override
	public List<WarehouseResponseDTO> findAll(Long companyId) {

		return repository.findByCompanyId(companyId).stream().map(x->mapToDTO(x)).collect(Collectors.toList());
	}

	/* Mapping */

	private WarehouseResponseDTO mapToDTO(Warehouse s) {
		return WarehouseResponseDTO.builder().id(s.getId()).name(s.getName())
				.mainContact(s.getMainContact()).email(s.getEmail()).contactNumber(s.getContactNumber())
				.address(s.getAddress()).active(s.getActive()).build();
		
	}
}
