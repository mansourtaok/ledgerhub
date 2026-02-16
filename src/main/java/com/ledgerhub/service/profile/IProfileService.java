package com.ledgerhub.service.profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ledgerhub.model.dto.profile.ProfileDTO;

public interface IProfileService {

	ProfileDTO create(ProfileDTO dto, Long userId);

	ProfileDTO getById(Long id);

	Page<ProfileDTO> getAll(String name, Long categoryId, Long typeId, Pageable pageable);

	ProfileDTO update(Long id, ProfileDTO dto, Long userId);
}
