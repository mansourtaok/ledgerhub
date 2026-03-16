package com.ledgerhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ledgerhub.model.db.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

	List<Warehouse> findByCompanyId(Long companyId);
}