package com.ledgerhub.service.usercompany.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.User;
import com.ledgerhub.model.db.UserCompany;
import com.ledgerhub.model.dto.usercompany.UserCompanyBulkRequestDTO;
import com.ledgerhub.model.dto.usercompany.UserCompanyRequestDTO;
import com.ledgerhub.model.dto.usercompany.UserCompanyResponseDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.UserCompanyRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.usercompany.IUserCompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCompanyService implements IUserCompanyService {

	private final UserCompanyRepository userCompanyRepository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;

	@Override
	@Transactional
	public UserCompanyResponseDTO assign(UserCompanyRequestDTO dto) {
		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		Company company = companyRepository.findById(dto.getCompanyId())
				.orElseThrow(() -> new IllegalArgumentException("Company not found"));

		if (userCompanyRepository.findByUserIdAndCompanyId(dto.getUserId(), dto.getCompanyId()).isPresent()) {
			throw new IllegalArgumentException("User is already assigned to this company");
		}

		boolean setDefault = Boolean.TRUE.equals(dto.getIsDefault());

		if (setDefault) {
			userCompanyRepository.clearDefaultForUser(dto.getUserId());
		}

		UserCompany userCompany = UserCompany.builder()
				.user(user)
				.company(company)
				.isDefault(setDefault)
				.build();

		return toDto(userCompanyRepository.save(userCompany));
	}

	@Override
	@Transactional
	public List<UserCompanyResponseDTO> assignBulk(UserCompanyBulkRequestDTO dto) {
		Company company = companyRepository.findById(dto.getCompanyId())
				.orElseThrow(() -> new IllegalArgumentException("Company not found"));

		return dto.getUsers().stream()
				.filter(entry -> userCompanyRepository.findByUserIdAndCompanyId(entry.getUserId(), dto.getCompanyId()).isEmpty())
				.map(entry -> {
					User user = userRepository.findById(entry.getUserId())
							.orElseThrow(() -> new IllegalArgumentException("User not found: " + entry.getUserId()));

					boolean setDefault = Boolean.TRUE.equals(entry.getIsDefault());
					if (setDefault) {
						userCompanyRepository.clearDefaultForUser(entry.getUserId());
					}

					UserCompany userCompany = UserCompany.builder()
							.user(user)
							.company(company)
							.isDefault(setDefault)
							.build();

					return toDto(userCompanyRepository.save(userCompany));
				})
				.toList();
	}

@Override
	@Transactional
	public UserCompanyResponseDTO setDefault(Long id) {
		UserCompany userCompany = userCompanyRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

		userCompanyRepository.clearDefaultForUser(userCompany.getUser().getId());
		userCompany.setIsDefault(true);
		return toDto(userCompanyRepository.save(userCompany));
	}

	@Override
	public void remove(Long id) {
		if (!userCompanyRepository.existsById(id)) {
			throw new IllegalArgumentException("Assignment not found");
		}
		userCompanyRepository.deleteById(id);
	}

	private UserCompanyResponseDTO toDto(UserCompany uc) {
		return UserCompanyResponseDTO.builder()
				.id(uc.getId())
				.userId(uc.getUser().getId())
				.userEmail(uc.getUser().getEmail())
				.companyId(uc.getCompany().getId())
				.companyName(uc.getCompany().getName())
				.isDefault(uc.getIsDefault())
				.createdAt(uc.getCreatedAt())
				.build();
	}
}
