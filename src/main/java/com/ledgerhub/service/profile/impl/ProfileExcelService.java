package com.ledgerhub.service.profile.impl;

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
import com.ledgerhub.model.db.Profile;
import com.ledgerhub.model.db.SystemLookup;
import com.ledgerhub.model.db.User;
import com.ledgerhub.repository.CompanyRepository;
import com.ledgerhub.repository.ProfileRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.repository.UserRepository;
import com.ledgerhub.service.profile.IProfileExcelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileExcelService implements IProfileExcelService {

	private final ProfileRepository profileRepository;
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

				String categoryCode = getString(row, 2);
				String typeCode = getString(row, 3);
				String currencyCode = getString(row, 4);
				String paymentTermCode = getString(row, 5);

				SystemLookup category = systemLookupRepository.findByCategoryAndCode("CATEGORY", categoryCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid category: " + categoryCode));

				SystemLookup type = systemLookupRepository.findByCategoryAndCode("TYPE", typeCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid type: " + typeCode));

				SystemLookup currency = systemLookupRepository.findByCategoryAndCode("CURRENCY", currencyCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid currency: " + currencyCode));

				SystemLookup paymentTerm = systemLookupRepository.findByCategoryAndCode("PAYMENT_TERM", paymentTermCode)
						.orElseThrow(() -> new IllegalArgumentException("Invalid payment term: " + paymentTermCode));

				Profile profile = Profile.builder().company(company).createdUser(createdUser).lastUpdateUser(lastUpdateUser)
						.name(getString(row, 0)).categoryId(category).typeId(type).address(getString(row, 1))
						.currencyId(currency).paymentTerm(paymentTerm)
						.contactNumber(getString(row, 7)).faxNumber(getString(row, 8)).taxNumber(getString(row, 9))
						.vendorAccount(getString(row, 10)).customerAccount(getString(row, 11))
						.notes(getString(row, 12)).build();

				profileRepository.save(profile);
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to import profile", e);
		}
	}

	@Override
	public byte[] exportProfilesByCompany(Long companyId) {

		List<Profile> profiles = profileRepository.findByCompanyId(companyId);

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Profiles");

			// Header
			Row header = sheet.createRow(0);
			String[] columns = { "Profile Name", "Address", "Category", "Type", "Currency", "Payment Term", 
					"Country", "Contact Number", "Fax Number", "Tax Number", "Vendor Account", "Customer Account", "Notes" };

			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
			}

			int rowIdx = 1;
			for (Profile profile : profiles) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(profile.getName());
				row.createCell(1).setCellValue(profile.getAddress());
				row.createCell(2).setCellValue(getCode(profile.getCategoryId()));
				row.createCell(3).setCellValue(getCode(profile.getTypeId()));
				row.createCell(4).setCellValue(getCode(profile.getCurrencyId()));
				row.createCell(5).setCellValue(getCode(profile.getPaymentTerm()));
				//row.createCell(6).setCellValue(profile.getCountry());
				row.createCell(7).setCellValue(profile.getContactNumber());
				row.createCell(8).setCellValue(profile.getFaxNumber());
				row.createCell(9).setCellValue(profile.getTaxNumber());
				row.createCell(10).setCellValue(profile.getVendorAccount());
				row.createCell(11).setCellValue(profile.getCustomerAccount());
				row.createCell(12).setCellValue(profile.getNotes());
			}

			// Auto-size columns
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);
			return out.toByteArray();

		} catch (IOException e) {
			throw new RuntimeException("Failed to export profiles", e);
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
