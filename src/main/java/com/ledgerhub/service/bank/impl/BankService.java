package com.ledgerhub.service.bank.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ledgerhub.model.db.Bank;
import com.ledgerhub.model.dto.BankDTO;
import com.ledgerhub.repository.BankRepository;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.CountryRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.service.bank.IBankService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BankService implements IBankService {

	private final BankRepository bankRepository;
	private final CompanyRepository companyRepository;
	private final SystemLookupRepository systemLookupRepository;
	private final CountryRepository countryRepository;

	@Override
	public BankDTO create(BankDTO dto) {
		Bank bank = mapToEntity(dto);
		return mapToDto(bankRepository.save(bank));
	}

	@Override
	public BankDTO update(Long id, BankDTO dto) {
		Bank bank = bankRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bank not found"));

		bank.setBankName(dto.getBankName());
		bank.setBankAccNbr(dto.getBankAccNbr());
		bank.setBankAccLabel(dto.getBankAccLabel());
		bank.setFinAccNbr(dto.getFinAccNbr());
		bank.setSwiftNbr(dto.getSwiftNbr());
		bank.setIbanNbr(dto.getIbanNbr());

		bank.setCurrency(systemLookupRepository.findById(dto.getCurrencyId())
				.orElseThrow(() -> new EntityNotFoundException("Currency not found")));

		bank.setCountry(countryRepository.findById(dto.getCountryId())
				.orElseThrow(() -> new EntityNotFoundException("Country not found")));

		return mapToDto(bank);
	}

	@Override
	@Transactional(readOnly = true)
	public BankDTO getById(Long id) {
		return bankRepository.findById(id).map(this::mapToDto)
				.orElseThrow(() -> new EntityNotFoundException("Bank not found"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<BankDTO> getByCompany(Long companyId) {
		return bankRepository.findByCompanyId(companyId).stream().map(this::mapToDto).toList();
	}

	@Override
	public void delete(Long id) {
		if (!bankRepository.existsById(id)) {
			throw new EntityNotFoundException("Bank not found");
		}
		bankRepository.deleteById(id);
	}

	/* ================= Mapping ================= */

	private Bank mapToEntity(BankDTO dto) {
		return Bank.builder()
				.company(companyRepository.findById(dto.getCompanyId())
						.orElseThrow(() -> new EntityNotFoundException("Company not found")))
				.currency(systemLookupRepository.findById(dto.getCurrencyId())
						.orElseThrow(() -> new EntityNotFoundException("Currency not found")))
				.country(countryRepository.findById(dto.getCountryId())
						.orElseThrow(() -> new EntityNotFoundException("Country not found")))
				.bankName(dto.getBankName()).bankAccNbr(dto.getBankAccNbr()).bankAccLabel(dto.getBankAccLabel())
				.finAccNbr(dto.getFinAccNbr()).swiftNbr(dto.getSwiftNbr()).ibanNbr(dto.getIbanNbr()).build();
	}

	private BankDTO mapToDto(Bank bank) {
		return BankDTO.builder().id(bank.getId()).companyId(bank.getCompany().getId())
				.currencyId(bank.getCurrency().getId()).countryId(bank.getCountry().getId())
				.bankName(bank.getBankName()).bankAccNbr(bank.getBankAccNbr()).bankAccLabel(bank.getBankAccLabel())
				.finAccNbr(bank.getFinAccNbr()).swiftNbr(bank.getSwiftNbr()).ibanNbr(bank.getIbanNbr()).build();
	}
}
