package com.ledgerhub.service;

import java.util.List;

import com.ledgerhub.model.dto.company.CompanyDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface ICompanyService {

	CompanyDTO create(CompanyDTO dto, HttpServletRequest request);

	CompanyDTO getById(Long id);

	List<CompanyDTO> getAll();

	CompanyDTO update(Long id, CompanyDTO dto, HttpServletRequest request);

	void deactivate(Long id);

	List<CompanyDTO> getByUserId(Long userId);
}
