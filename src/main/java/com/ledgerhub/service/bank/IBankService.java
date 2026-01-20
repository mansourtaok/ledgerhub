package com.ledgerhub.service.bank;

import java.util.List;

import com.ledgerhub.model.dto.BankDTO;

public interface IBankService {

	BankDTO create(Long companyId, BankDTO dto);

	BankDTO update(Long companyId, Long bankId, BankDTO dto);

	BankDTO getById(Long companyId, Long bankId);

	List<BankDTO> getByCompany(Long companyId);

	void delete(Long companyId, Long bankId);
}
