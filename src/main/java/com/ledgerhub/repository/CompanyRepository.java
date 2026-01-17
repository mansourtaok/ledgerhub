package com.ledgerhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
