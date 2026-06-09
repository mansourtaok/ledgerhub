package com.ledgerhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

	List<Document> findByReferenceId(Long referenceId);
}
