package com.ledgerhub.service.profile.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.Profile;
import com.ledgerhub.model.dto.profile.ProfileDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.ProfileRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.profile.IProfileService;
import com.ledgerhub.utils.ProfileSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

	private final ProfileRepository profileRepository;
	private final CompanyRepository companyRepository;
	private final SystemLookupRepository systemLookupRepository;
	private final UserRepository userRepository;

	@Override
	public ProfileDTO create(ProfileDTO dto, Long userId) {
		Profile profile = mapToEntity(dto);
		profile.setCreatedUser(
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
		return mapToDTO(profileRepository.save(profile));
	}

	@Override
	public ProfileDTO getById(Long id) {
		return profileRepository.findById(id).map(this::mapToDTO)
				.orElseThrow(() -> new RuntimeException("Profile not found"));
	}

	@Override
	public Page<ProfileDTO> getAll(String name, Long categoryId, Long typeId, Pageable pageable) {

		Specification<Profile> spec = Specification.where(ProfileSpecification.fullNameLike(name))
				.and(ProfileSpecification.categoryEquals(categoryId)).and(ProfileSpecification.typeEquals(typeId));

		return profileRepository.findAll(spec, pageable).map(this::mapToDTO);
	}

	@Override
	public ProfileDTO update(Long id, ProfileDTO dto, Long userId) {
		Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

		profile.setName(dto.getName());
		profile.setAddress(dto.getAddress());
		profile.setContactNumber(dto.getContactNumber());
		profile.setFaxNumber(dto.getFaxNumber());
		profile.setTaxNumber(dto.getTaxNumber());
		profile.setVendorAccount(dto.getVendorAccountNumber());
		profile.setCustomerAccount(dto.getCustomerAccountNumber());

		profile.setLastUpdateUser(
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));

		if (dto.getCompanyId() != null) {
			profile.setCompany(companyRepository.findById(dto.getCompanyId())
					.orElseThrow(() -> new RuntimeException("Company not found")));
		}

		if (dto.getCategoryId() != null) {
			profile.setCategoryId(systemLookupRepository.findById(dto.getCategoryId())
					.orElseThrow(() -> new RuntimeException("Category not found")));
		}

		if (dto.getTypeId() != null) {
			profile.setTypeId(systemLookupRepository.findById(dto.getTypeId())
					.orElseThrow(() -> new RuntimeException("Type not found")));
		}

		if (dto.getCurrencyId() != null) {
			profile.setCurrencyId(systemLookupRepository.findById(dto.getCurrencyId())
					.orElseThrow(() -> new RuntimeException("Currency not found")));
		}

		if (dto.getPaymentTerm() != null) {
			profile.setPaymentTerm(systemLookupRepository.findById(dto.getPaymentTerm())
					.orElseThrow(() -> new RuntimeException("Payment Term not found")));
		}

		return mapToDTO(profileRepository.save(profile));
	}

	/* Mapping */

	private Profile mapToEntity(ProfileDTO dto) {
		Profile profile = Profile.builder().name(dto.getName()).address(dto.getAddress())
				.contactNumber(dto.getContactNumber()).faxNumber(dto.getFaxNumber()).taxNumber(dto.getTaxNumber())
				.vendorAccount(dto.getVendorAccountNumber()).customerAccount(dto.getCustomerAccountNumber())
				.countryId(dto.getCountryId()).build();

		if (dto.getCompanyId() != null) {
			profile.setCompany(companyRepository.findById(dto.getCompanyId())
					.orElseThrow(() -> new RuntimeException("Company not found")));
		}

		if (dto.getCategoryId() != null) {
			profile.setCategoryId(systemLookupRepository.findById(dto.getCategoryId())
					.orElseThrow(() -> new RuntimeException("Category not found")));
		}

		if (dto.getTypeId() != null) {
			profile.setTypeId(systemLookupRepository.findById(dto.getTypeId())
					.orElseThrow(() -> new RuntimeException("Type not found")));
		}

		if (dto.getCurrencyId() != null) {
			profile.setCurrencyId(systemLookupRepository.findById(dto.getCurrencyId())
					.orElseThrow(() -> new RuntimeException("Currency not found")));
		}

		if (dto.getPaymentTerm() != null) {
			profile.setPaymentTerm(systemLookupRepository.findById(dto.getPaymentTerm())
					.orElseThrow(() -> new RuntimeException("Payment Term not found")));
		}

		return profile;
	}

	private ProfileDTO mapToDTO(Profile s) {
		return ProfileDTO.builder().id(s.getId()).companyId(s.getCompany() != null ? s.getCompany().getId() : null)
				.name(s.getName()).categoryId(s.getCategoryId().getId()).typeId(s.getTypeId().getId())
				.address(s.getAddress()).countryId(s.getCountryId()).currencyId(s.getCurrencyId().getId())
				.paymentTerm(s.getPaymentTerm().getId()).contactNumber(s.getContactNumber()).faxNumber(s.getFaxNumber())
				.taxNumber(s.getTaxNumber()).vendorAccountNumber(s.getVendorAccount())
				.customerAccountNumber(s.getCustomerAccount()).build();
	}

}
