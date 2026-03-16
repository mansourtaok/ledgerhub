package com.ledgerhub.service.warehouse;

import java.util.List;

import com.ledgerhub.model.dto.warehouse.WarehouseRequestDTO;
import com.ledgerhub.model.dto.warehouse.WarehouseResponseDTO;

public interface IWarehouseService {

	List<WarehouseResponseDTO> findAll(Long companyId);

	WarehouseResponseDTO create(WarehouseRequestDTO dto, Long companyId);

	WarehouseResponseDTO update(Long id, WarehouseRequestDTO dto);

	void delete(Long id);

}
