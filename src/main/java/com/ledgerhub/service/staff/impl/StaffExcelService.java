package com.ledgerhub.service.staff.impl;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.model.db.Company;
import com.ledgerhub.model.db.Staff;
import com.ledgerhub.model.db.SystemLookup;
import com.ledgerhub.model.db.User;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.StaffRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.staff.IStaffExcelService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffExcelService implements IStaffExcelService {

	private final StaffRepository staffRepository;
	private final CompanyRepository companyRepository;
	private final SystemLookupRepository systemLookupRepository;
	private final UserRepository userRepository;

	public void importFromExcel(Long companyId, Long createdUserId, Long lastUpdateUserId, MultipartFile file) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new IllegalArgumentException("Company not found"));

		User createdUser = userRepository.findById(createdUserId)
				.orElseThrow(() -> new IllegalArgumentException("Created user not found"));

		User lastUpdateUser = userRepository.findById(lastUpdateUserId)
				.orElseThrow(() -> new IllegalArgumentException("Last update user not found"));

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				String genderCode = getString(row, 6);
				String jobCode = getString(row, 7);

				SystemLookup gender = systemLookupRepository.findByCategoryAndCode("GENDER", genderCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid gender: " + genderCode));

				SystemLookup jobDescription = systemLookupRepository.findByCategoryAndCode("JOB_DESCRIPTION", jobCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid job: " + jobCode));

				Staff staff = Staff.builder().company(company).createdUser(createdUser).lastUpdateUser(lastUpdateUser)
						.gender(gender).jobDescription(jobDescription).fullName(getString(row, 0))
						.address(getString(row, 1)).contactNumber(getString(row, 2)).email(getString(row, 3))
						.joinDate(getDate(row, 4)).leaveDate(getDate(row, 5)).haveCnss(getBoolean(row, 8))
						.cnssNumber(getString(row, 9)).notes(getString(row, 10)).active(true).build();

				staffRepository.save(staff);
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to import staff", e);
		}
	}

	/* ================= Helpers ================= */

	private String getString(Row row, int index) {
		Cell cell = row.getCell(index);
		return cell == null ? null : cell.getStringCellValue().trim();
	}

	private LocalDate getDate(Row row, int index) {
		Cell cell = row.getCell(index);
		return cell == null ? null : cell.getLocalDateTimeCellValue().toLocalDate();
	}

	private Boolean getBoolean(Row row, int index) {
		Cell cell = row.getCell(index);
		return cell != null && cell.getBooleanCellValue();
	}
}
