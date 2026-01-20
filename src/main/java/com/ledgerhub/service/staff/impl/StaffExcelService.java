package com.ledgerhub.service.staff.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffExcelService implements IStaffExcelService {

	private final StaffRepository staffRepository;
	private final CompanyRepository companyRepository;
	private final SystemLookupRepository systemLookupRepository;
	private final UserRepository userRepository;

	@Override
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

	@Override
	public byte[] exportStaffsByCompany(Long companyId) {

		List<Staff> staffs = staffRepository.findByCompanyId(companyId);

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Staffs");

			// Header
			Row header = sheet.createRow(0);
			String[] columns = { "Full Name", "Address", "Contact Number", "Email", "Join Date", "Leave Date", "Gender",
					"Job Description", "Have CNSS", "CNSS Number", "Notes", "Active" };

			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
			}

			int rowIdx = 1;
			for (Staff staff : staffs) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(staff.getFullName());
				row.createCell(1).setCellValue(staff.getAddress());
				row.createCell(2).setCellValue(staff.getContactNumber());
				row.createCell(3).setCellValue(staff.getEmail());
				row.createCell(4).setCellValue(toString(staff.getJoinDate()));
				row.createCell(5).setCellValue(toString(staff.getLeaveDate()));
				row.createCell(6).setCellValue(getCode(staff.getGender()));
				row.createCell(7).setCellValue(getCode(staff.getJobDescription()));
				row.createCell(8).setCellValue(Boolean.TRUE.equals(staff.getHaveCnss()));
				row.createCell(9).setCellValue(staff.getCnssNumber());
				row.createCell(10).setCellValue(staff.getNotes());
				row.createCell(11).setCellValue(Boolean.TRUE.equals(staff.getActive()));
			}

			// Auto-size columns
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);
			return out.toByteArray();

		} catch (IOException e) {
			throw new RuntimeException("Failed to export staffs", e);
		}
	}

	private String toString(LocalDate date) {
		return date == null ? "" : date.toString();
	}

	private String getCode(SystemLookup lookup) {
		return lookup == null ? "" : lookup.getCode();
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
