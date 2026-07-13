package com.ledgerhub.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.config.jwt.JwtTokenUtil;
import com.ledgerhub.model.db.BaseEntity;
import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.Country;
import com.ledgerhub.model.db.Document;
import com.ledgerhub.model.db.User;
import com.ledgerhub.model.dto.company.CompanyDTO;
import com.ledgerhub.model.dto.country.CountryDTO;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.CountryRepository;
import com.ledgerhub.repository.DocumentRepository;
import com.ledgerhub.repository.EntityRepository;
import com.ledgerhub.repository.UserCompanyRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.ICompanyService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {

	private final CompanyRepository companyRepository;
	private final CountryRepository countryRepository;
	private final EntityRepository entityRepository;
	private final DocumentRepository documentRepository;
	private final UserCompanyRepository userCompanyRepository;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@org.springframework.beans.factory.annotation.Value("${app.upload-dir}")
	private String uploadDir;

	@org.springframework.beans.factory.annotation.Value("${app.base-url}")
	private String baseUrl;

	@Transactional
	@Override
	public CompanyDTO create(CompanyDTO dto, HttpServletRequest request) {
		BaseEntity entity = entityRepository.save(BaseEntity.builder().entityType("COMPANY").build());

		MultipartFile image = dto.getImage();
		if (image != null && !image.isEmpty()) {
			String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path path = Paths.get(uploadDir, filename);
			try {
				Files.createDirectories(path.getParent());
				Files.write(path, image.getBytes());
			} catch (Exception e) {
				throw new RuntimeException("File upload failed");
			}
			Document doc = new Document();
			doc.setFilename(filename);
			doc.setFilepath(path.toString());
			doc.setReferenceId(entity.getId());
			documentRepository.save(doc);
		}

		User user = userRepository.findById(jwtTokenUtil.getUserIdFromToken(request.getHeader("Authorization")))
				.orElseThrow(() -> new RuntimeException("User not found"));

		Company company = mapToEntity(dto);
		company.setEntityId(entity.getId());
		company.setCreatedUser(user);
		company.setUpdatedUser(user);
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

	@Transactional
	@Override
	public CompanyDTO update(Long id, CompanyDTO dto, HttpServletRequest request) {
		Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));

		User user = userRepository.findById(jwtTokenUtil.getUserIdFromToken(request.getHeader("Authorization")))
				.orElseThrow(() -> new RuntimeException("User not found"));
		company.setUpdatedUser(user);

		company.setName(dto.getName());
		company.setEmail(dto.getEmail());
		company.setPhone(dto.getPhone());
		company.setAddress(dto.getAddress());
		company.setTaxNumber(dto.getTaxNumber());
		company.setHeader(dto.getHeader());
		company.setFooter(dto.getFooter());
		company.setWebsite(dto.getWebsite());
		company.setActive(dto.getActive());

		if (dto.getCountry() != null && dto.getCountry().getId() != null) {
			Country country = countryRepository.findById(dto.getCountry().getId())
					.orElseThrow(() -> new RuntimeException("Country not found"));
			company.setCountry(country);
		}

		MultipartFile image = dto.getImage();
		if (image != null && !image.isEmpty()) {
			if (company.getEntityId() == null) {
				BaseEntity entity = entityRepository.save(BaseEntity.builder().entityType("COMPANY").build());
				company.setEntityId(entity.getId());
			} else {
				documentRepository.findByReferenceId(company.getEntityId())
						.forEach(doc -> documentRepository.delete(doc));
			}
			String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path path = Paths.get(uploadDir, filename);
			try {
				Files.createDirectories(path.getParent());
				Files.write(path, image.getBytes());
			} catch (Exception e) {
				throw new RuntimeException("File upload failed");
			}
			Document doc = new Document();
			doc.setFilename(filename);
			doc.setFilepath(path.toString());
			doc.setReferenceId(company.getEntityId());
			documentRepository.save(doc);
		}

		return mapToDTO(companyRepository.save(company));
	}

	@Override
	public void deactivate(Long id) {
		Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
		company.setActive(false);
		companyRepository.save(company);
	}

	@Override
	public List<CompanyDTO> getByUserId(Long userId) {
		return userCompanyRepository.findByUserId(userId).stream()
				.map(uc -> {
					CompanyDTO dto = mapToDTO(uc.getCompany());
					dto.setIsDefault(uc.getIsDefault());
					return dto;
				})
				.toList();
	}

	private Company mapToEntity(CompanyDTO dto) {
		Company company = Company.builder().name(dto.getName()).email(dto.getEmail()).phone(dto.getPhone())
				.address(dto.getAddress()).taxNumber(dto.getTaxNumber()).header(dto.getHeader()).footer(dto.getFooter())
				.website(dto.getWebsite()).active(dto.getActive()).build();

		if (dto.getCountry() != null && dto.getCountry().getId() != null) {
			Country country = countryRepository.findById(dto.getCountry().getId())
					.orElseThrow(() -> new RuntimeException("Country not found"));
			company.setCountry(country);
		}
		return company;
	}

	private CompanyDTO mapToDTO(Company entity) {
		String imageUrl = null;
		if (entity.getEntityId() != null) {
			imageUrl = documentRepository.findByReferenceId(entity.getEntityId()).stream()
					.map(doc -> baseUrl + "/images/" + doc.getFilename())
					.findFirst().orElse(null);
		}
		CountryDTO countryDTO = null;
		if (entity.getCountry() != null) {
			countryDTO = CountryDTO.builder()
					.id(entity.getCountry().getId())
					.name(entity.getCountry().getName())
					.build();
		}
		return CompanyDTO.builder().id(entity.getId()).name(entity.getName()).email(entity.getEmail())
				.phone(entity.getPhone()).address(entity.getAddress()).taxNumber(entity.getTaxNumber())
				.header(entity.getHeader()).footer(entity.getFooter()).website(entity.getWebsite())
				.country(countryDTO).active(entity.getActive())
				.imageUrl(imageUrl)
				.createdDate(entity.getCreatedDate()).updatedDate(entity.getUpdatedDate())
				.createdUser(userDisplayName(entity.getCreatedUser()))
				.updatedUser(userDisplayName(entity.getUpdatedUser()))
				.build();
	}

	private String userDisplayName(User user) {
		if (user == null) {
			return null;
		}
		String fullName = String.join(" ",
				user.getFirstName() != null ? user.getFirstName() : "",
				user.getLastName() != null ? user.getLastName() : "").trim();
		return fullName.isEmpty() ? user.getEmail() : fullName;
	}
}
