package com.ledgerhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {

	List<Bank> findByCompanyId(Long companyId);
}
