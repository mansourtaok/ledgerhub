package com.ledgerhub.service.bank;

import java.util.List;

import com.ledgerhub.model.dto.BankDTO;

public interface IBankService {

	BankDTO create(BankDTO dto);

	BankDTO update(Long id, BankDTO dto);

	BankDTO getById(Long id);

	List<BankDTO> getByCompany(Long companyId);

	void delete(Long id);
}
