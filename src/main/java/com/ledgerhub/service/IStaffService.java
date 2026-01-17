package com.ledgerhub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ledgerhub.model.dto.staff.StaffDTO;

public interface IStaffService {

	StaffDTO create(StaffDTO dto, Long userId);

	StaffDTO getById(Long id);

	Page<StaffDTO> getAll(String fullName, Long jobDescriptionId, Pageable pageable);

	StaffDTO update(Long id, StaffDTO dto, Long userId);

	void deactivate(Long id);
}
