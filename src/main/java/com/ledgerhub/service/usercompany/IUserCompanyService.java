package com.ledgerhub.service.usercompany;

import java.util.List;

import com.ledgerhub.model.dto.usercompany.UserCompanyBulkRequestDTO;
import com.ledgerhub.model.dto.usercompany.UserCompanyRequestDTO;
import com.ledgerhub.model.dto.usercompany.UserCompanyResponseDTO;

public interface IUserCompanyService {

	UserCompanyResponseDTO assign(UserCompanyRequestDTO dto);

	List<UserCompanyResponseDTO> assignBulk(UserCompanyBulkRequestDTO dto);

UserCompanyResponseDTO setDefault(Long id);

	void remove(Long id);
}
