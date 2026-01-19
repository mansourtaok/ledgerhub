package com.ledgerhub.service.staff.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.Staff;
import com.ledgerhub.model.dto.staff.StaffDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.StaffRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.staff.IStaffService;
import com.ledgerhub.utils.StaffSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffService {

	private final StaffRepository staffRepository;
	private final CompanyRepository companyRepository;
	private final SystemLookupRepository systemLookupRepository;
	private final UserRepository userRepository;

	@Override
	public StaffDTO create(StaffDTO dto, Long userId) {
		Staff staff = mapToEntity(dto);
		staff.setCreatedUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
		return mapToDTO(staffRepository.save(staff));
	}

	@Override
	public StaffDTO getById(Long id) {
		return staffRepository.findById(id).map(this::mapToDTO)
				.orElseThrow(() -> new RuntimeException("Staff not found"));
	}

	@Override
	public Page<StaffDTO> getAll(String fullName, Long jobDescriptionId, Pageable pageable) {

		Specification<Staff> spec = Specification.where(StaffSpecification.fullNameLike(fullName))
				.and(StaffSpecification.jobDescriptionEquals(jobDescriptionId));

		return staffRepository.findAll(spec, pageable).map(this::mapToDTO);
	}

	@Override
	public StaffDTO update(Long id, StaffDTO dto, Long userId) {
		Staff staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));

		staff.setFullName(dto.getFullName());
		staff.setAddress(dto.getAddress());
		staff.setContactNumber(dto.getContactNumber());
		staff.setEmail(dto.getEmail());
		staff.setJoinDate(dto.getJoinDate());
		staff.setLeaveDate(dto.getLeaveDate());
		staff.setHaveCnss(dto.getHaveCnss());
		staff.setCnssNumber(dto.getCnssNumber());
		staff.setNotes(dto.getNotes());
		staff.setActive(dto.getActive());

		staff.setLastUpdateUser(
				userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));

		if (dto.getCompanyId() != null) {
			staff.setCompany(companyRepository.findById(dto.getCompanyId())
					.orElseThrow(() -> new RuntimeException("Company not found")));
		}

		if (dto.getGenderId() != null) {
			staff.setGender(systemLookupRepository.findById(dto.getGenderId())
					.orElseThrow(() -> new RuntimeException("Gender not found")));
		}

		staff.setJobDescription(systemLookupRepository.findById(dto.getJobDescriptionId())
				.orElseThrow(() -> new RuntimeException("Job description not found")));

		return mapToDTO(staffRepository.save(staff));
	}

	@Override
	public void deactivate(Long id) {
		Staff staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
		staff.setActive(false);
		staffRepository.save(staff);
	}

	/* Mapping */

	private Staff mapToEntity(StaffDTO dto) {
		Staff staff = Staff.builder().fullName(dto.getFullName()).address(dto.getAddress())
				.contactNumber(dto.getContactNumber()).email(dto.getEmail()).joinDate(dto.getJoinDate())
				.leaveDate(dto.getLeaveDate()).haveCnss(dto.getHaveCnss()).cnssNumber(dto.getCnssNumber())
				.notes(dto.getNotes()).active(dto.getActive()).build();

		if (dto.getCompanyId() != null) {
			staff.setCompany(companyRepository.findById(dto.getCompanyId())
					.orElseThrow(() -> new RuntimeException("Company not found")));
		}

		if (dto.getGenderId() != null) {
			staff.setGender(systemLookupRepository.findById(dto.getGenderId())
					.orElseThrow(() -> new RuntimeException("Gender not found")));
		}

		staff.setJobDescription(systemLookupRepository.findById(dto.getJobDescriptionId())
				.orElseThrow(() -> new RuntimeException("Job description not found")));

		return staff;
	}

	private StaffDTO mapToDTO(Staff s) {
		return StaffDTO.builder().id(s.getId()).companyId(s.getCompany() != null ? s.getCompany().getId() : null)
				.fullName(s.getFullName()).genderId(s.getGender() != null ? s.getGender().getId() : null)
				.jobDescriptionId(s.getJobDescription().getId()).address(s.getAddress())
				.contactNumber(s.getContactNumber()).email(s.getEmail()).joinDate(s.getJoinDate())
				.leaveDate(s.getLeaveDate()).haveCnss(s.getHaveCnss()).cnssNumber(s.getCnssNumber()).notes(s.getNotes())
				.active(s.getActive()).build();
	}
}
