package com.ledgerhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
