package com.ledgerhub.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ledgerhub.model.db.items.ItemCategory;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

	@Query("SELECT i FROM ItemCategory i "
			+ "WHERE i.company.id = :companyId AND  (:parentId IS NULL OR i.parentId = :parentId) "
			+ "AND (:label IS NULL OR :label = '' OR i.label LIKE %:label%)")
	Page<ItemCategory> search(@Param("companyId") Long companyId, @Param("label") String label,
			@Param("parentId") Integer parentId, Pageable pageable);

	Optional<ItemCategory> findByCompanyIdAndLabel(Long companyId, String label);
}
