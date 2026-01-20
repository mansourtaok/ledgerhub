package com.ledgerhub.service.staff;

import org.springframework.web.multipart.MultipartFile;

public interface IStaffExcelService {

	void importFromExcel(Long companyId, Long createdUserId, Long lastUpdateUserId, MultipartFile file);

	byte[] exportStaffsByCompany(Long companyId);
}
