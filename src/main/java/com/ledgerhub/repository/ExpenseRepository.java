package com.ledgerhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ledgerhub.model.db.expenses.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

	//List<Expense> findByCompanyId(Long companyId);
}