package com.ledgerhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.BaseEntity;

public interface EntityRepository extends JpaRepository<BaseEntity, Long> {
}