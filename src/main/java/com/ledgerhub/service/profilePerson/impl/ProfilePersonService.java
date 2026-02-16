package com.ledgerhub.service.profilePerson.impl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ledgerhub.model.db.ProfilePerson;
import com.ledgerhub.model.db.SystemLookup;
import com.ledgerhub.model.dto.ProfilePersonDTO;
import com.ledgerhub.repository.ProfilePersonRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.service.profilePerson.IProfilePersonService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilePersonService implements IProfilePersonService {

	private final ProfilePersonRepository profilePersonRepository;
	private final SystemLookupRepository systemLookupRepository;

	/* ================= CREATE ================= */

	@Override
	public ProfilePersonDTO create(Long companyId, ProfilePersonDTO dto) {

		SystemLookup jobeDescription = systemLookupRepository.findById(dto.getJobDescriptionId())
				.orElseThrow(() -> new EntityNotFoundException("Job Description not found"));

		ProfilePerson profilePerson = ProfilePerson.builder().companyId(dto.getCompanyId())
				.profileId(dto.getProfileId()).jobDescription(jobeDescription).name(dto.getName()).email(dto.getEmail())
				.contactNumber(dto.getContactNumber()).extension(dto.getExtension()).build();

		profilePersonRepository.save(profilePerson);
		return mapToDto(profilePerson);
	}

	/* ================= UPDATE ================= */

	@Override
	public ProfilePersonDTO update(Long profileId, Long profilePersonId, ProfilePersonDTO dto) {

		ProfilePerson profilePerson = getProfilePersonForProfile(profileId, profilePersonId);

		profilePerson.setName(dto.getName());
		profilePerson.setEmail(dto.getEmail());
		profilePerson.setContactNumber(dto.getContactNumber());
		profilePerson.setExtension(dto.getExtension());

		profilePerson.setJobDescription(systemLookupRepository.findById(dto.getJobDescriptionId())
				.orElseThrow(() -> new EntityNotFoundException("Job Description not found")));

		return mapToDto(profilePerson);
	}

	/* ================= GET BY ID ================= */

	@Override
	@Transactional(readOnly = true)
	public ProfilePersonDTO getById(Long profileId, Long profilePersonId) {

		ProfilePerson profilePerson = getProfilePersonForProfile(profileId, profilePersonId);
		return mapToDto(profilePerson);
	}

	/* ================= LIST BY COMPANY ================= */

	@Override
	@Transactional(readOnly = true)
	public List<ProfilePersonDTO> getByCompany(Long companyId) {

		return profilePersonRepository.findByProfileId(companyId).stream().map(this::mapToDto).toList();
	}

	/* ================= DELETE ================= */

	@Override
	public void delete(Long profileId, Long profilePersonId) {

		ProfilePerson profilePerson = getProfilePersonForProfile(profileId, profilePersonId);
		profilePersonRepository.delete(profilePerson);
	}

	/* ================= INTERNAL HELPERS ================= */

	/**
	 * Ensures: - Profile Person exists - Profile Person belongs to the given
	 * profile
	 */
	private ProfilePerson getProfilePersonForProfile(Long profileId, Long profilePersonId) {

		ProfilePerson profilePerson = profilePersonRepository.findById(profilePersonId)
				.orElseThrow(() -> new EntityNotFoundException("Profile Person not found"));

		if (!profilePerson.getProfileId().equals(profileId)) {
			throw new AccessDeniedException("Profile Person does not belong to the given profile");
		}

		return profilePerson;
	}

	private ProfilePersonDTO mapToDto(ProfilePerson profilePerson) {

		return ProfilePersonDTO.builder().id(profilePerson.getId()).name(profilePerson.getName())
				.email(profilePerson.getEmail()).contactNumber(profilePerson.getContactNumber())
				.extension(profilePerson.getExtension()).profileId(profilePerson.getProfileId())
				.companyId(profilePerson.getCompanyId()).build();
	}
}
