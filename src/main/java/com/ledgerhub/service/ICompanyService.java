package com.ledgerhub.service;

import java.util.List;

import com.ledgerhub.model.dto.company.CompanyDTO;

public interface ICompanyService {

	CompanyDTO create(CompanyDTO dto);

	CompanyDTO getById(Long id);

	List<CompanyDTO> getAll();

	CompanyDTO update(Long id, CompanyDTO dto);

	void deactivate(Long id);
}
