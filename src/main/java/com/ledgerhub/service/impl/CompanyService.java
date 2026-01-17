package com.ledgerhub.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.Country;
import com.ledgerhub.model.dto.company.CompanyDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.CountryRepository;
import com.ledgerhub.service.ICompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {

	private final CompanyRepository companyRepository;
	private final CountryRepository countryRepository;

	@Override
	public CompanyDTO create(CompanyDTO dto) {
		Company company = mapToEntity(dto);
		return mapToDTO(companyRepository.save(company));
	}

	@Override
	public CompanyDTO getById(Long id) {
		Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
		return mapToDTO(company);
	}

	@Override
	public List<CompanyDTO> getAll() {
		return companyRepository.findAll().stream().map(this::mapToDTO).toList();
	}

	@Override
	public CompanyDTO update(Long id, CompanyDTO dto) {
		Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));

		company.setName(dto.getName());
		company.setEmail(dto.getEmail());
		company.setPhone(dto.getPhone());
		company.setAddress(dto.getAddress());
		company.setTaxNumber(dto.getTaxNumber());
		company.setHeader(dto.getHeader());
		company.setFooter(dto.getFooter());
		company.setActive(dto.getActive());

		if (dto.getCountryId() != null) {
			Country country = countryRepository.findById(dto.getCountryId())
					.orElseThrow(() -> new RuntimeException("Country not found"));
			company.setCountry(country);
		}

		return mapToDTO(companyRepository.save(company));
	}

	@Override
	public void deactivate(Long id) {
		Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
		company.setActive(false);
		companyRepository.save(company);
	}

	private Company mapToEntity(CompanyDTO dto) {
		Company company = Company.builder().name(dto.getName()).email(dto.getEmail()).phone(dto.getPhone())
				.address(dto.getAddress()).taxNumber(dto.getTaxNumber()).header(dto.getHeader()).footer(dto.getFooter())
				.active(dto.getActive()).build();

		if (dto.getCountryId() != null) {
			Country country = countryRepository.findById(dto.getCountryId())
					.orElseThrow(() -> new RuntimeException("Country not found"));
			company.setCountry(country);
		}
		return company;
	}

	private CompanyDTO mapToDTO(Company entity) {
		return CompanyDTO.builder().id(entity.getId()).name(entity.getName()).email(entity.getEmail())
				.phone(entity.getPhone()).address(entity.getAddress()).taxNumber(entity.getTaxNumber())
				.header(entity.getHeader()).footer(entity.getFooter())
				.countryId(entity.getCountry() != null ? entity.getCountry().getId() : null).active(entity.getActive())
				.build();
	}
}
