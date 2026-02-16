package com.ledgerhub.service.profile;

import org.springframework.web.multipart.MultipartFile;

public interface IProfileExcelService {

	void importFromExcel(Long companyId, Long createdUserId, Long lastUpdateUserId, MultipartFile file);

	byte[] exportProfilesByCompany(Long companyId);
}
