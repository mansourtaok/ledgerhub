package com.ledgerhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.SystemLookup;

public interface SystemLookupRepository extends JpaRepository<SystemLookup, Long> {

	Optional<SystemLookup> findByCategoryAndCode(String category, String code);

	List<SystemLookup> findByCategoryAndIsActiveTrue(String category);
}
