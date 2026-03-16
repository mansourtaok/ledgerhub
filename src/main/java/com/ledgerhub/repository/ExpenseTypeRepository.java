package com.ledgerhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ledgerhub.model.db.expenses.ExpenseType;

public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long>, JpaSpecificationExecutor<ExpenseType> {

	List<ExpenseType> findByCompanyId(Long companyId);
}