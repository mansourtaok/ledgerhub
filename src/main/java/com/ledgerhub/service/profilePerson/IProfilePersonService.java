package com.ledgerhub.service.profilePerson;

import java.util.List;

import com.ledgerhub.model.dto.ProfilePersonDTO;

public interface IProfilePersonService {

	ProfilePersonDTO create(Long companyId, ProfilePersonDTO dto);

	ProfilePersonDTO update(Long companyId, Long profilePersonId, ProfilePersonDTO dto);

	ProfilePersonDTO getById(Long companyId, Long profilePersonId);

	List<ProfilePersonDTO> getByCompany(Long companyId);

	void delete(Long companyId, Long profilePersonId);
}
