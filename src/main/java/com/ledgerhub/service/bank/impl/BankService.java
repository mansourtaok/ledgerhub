package com.ledgerhub.service.bank.impl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ledgerhub.model.db.Bank;
import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.Country;
import com.ledgerhub.model.db.SystemLookup;
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

	/* ================= CREATE ================= */

	@Override
	public BankDTO create(Long companyId, BankDTO dto) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new EntityNotFoundException("Company not found"));

		SystemLookup currency = systemLookupRepository.findById(dto.getCurrencyId())
				.orElseThrow(() -> new EntityNotFoundException("Currency not found"));

		Country country = countryRepository.findById(dto.getCountryId())
				.orElseThrow(() -> new EntityNotFoundException("Country not found"));

		Bank bank = Bank.builder().company(company).currency(currency).country(country).bankName(dto.getBankName())
				.bankAccNbr(dto.getBankAccNbr()).bankAccLabel(dto.getBankAccLabel()).finAccNbr(dto.getFinAccNbr())
				.swiftNbr(dto.getSwiftNbr()).ibanNbr(dto.getIbanNbr()).build();

		bankRepository.save(bank);
		return mapToDto(bank);
	}

	/* ================= UPDATE ================= */

	@Override
	public BankDTO update(Long companyId, Long bankId, BankDTO dto) {

		Bank bank = getBankForCompany(companyId, bankId);

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

	/* ================= GET BY ID ================= */

	@Override
	@Transactional(readOnly = true)
	public BankDTO getById(Long companyId, Long bankId) {

		Bank bank = getBankForCompany(companyId, bankId);
		return mapToDto(bank);
	}

	/* ================= LIST BY COMPANY ================= */

	@Override
	@Transactional(readOnly = true)
	public List<BankDTO> getByCompany(Long companyId) {

		return bankRepository.findByCompanyId(companyId).stream().map(this::mapToDto).toList();
	}

	/* ================= DELETE ================= */

	@Override
	public void delete(Long companyId, Long bankId) {

		Bank bank = getBankForCompany(companyId, bankId);
		bankRepository.delete(bank);
	}

	/* ================= INTERNAL HELPERS ================= */

	/**
	 * Ensures: - Bank exists - Bank belongs to the given company
	 */
	private Bank getBankForCompany(Long companyId, Long bankId) {

		Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new EntityNotFoundException("Bank not found"));

		if (!bank.getCompany().getId().equals(companyId)) {
			throw new AccessDeniedException("Bank does not belong to the given company");
		}

		return bank;
	}

	private BankDTO mapToDto(Bank bank) {

		return BankDTO.builder().id(bank.getId()).bankName(bank.getBankName()).bankAccNbr(bank.getBankAccNbr())
				.bankAccLabel(bank.getBankAccLabel()).finAccNbr(bank.getFinAccNbr()).swiftNbr(bank.getSwiftNbr())
				.ibanNbr(bank.getIbanNbr()).currencyId(bank.getCurrency().getId()).countryId(bank.getCountry().getId())
				.build();
	}
}
