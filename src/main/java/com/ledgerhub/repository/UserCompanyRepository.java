package com.ledgerhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ledgerhub.model.db.UserCompany;

public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {

	List<UserCompany> findByUserId(Long userId);

	Optional<UserCompany> findByUserIdAndCompanyId(Long userId, Long companyId);

	@Modifying
	@Query("UPDATE UserCompany uc SET uc.isDefault = false WHERE uc.user.id = :userId")
	void clearDefaultForUser(@Param("userId") Long userId);
}
