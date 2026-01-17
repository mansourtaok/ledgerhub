package com.ledgerhub.utils;

import org.springframework.data.jpa.domain.Specification;

import com.ledgerhub.model.db.Staff;

public class StaffSpecification {

	public static Specification<Staff> fullNameLike(String fullName) {
		return (root, query, cb) -> fullName == null ? null
				: cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
	}

	public static Specification<Staff> jobDescriptionEquals(Long jobDescriptionId) {
		return (root, query, cb) -> jobDescriptionId == null ? null
				: cb.equal(root.get("jobDescription").get("id"), jobDescriptionId);
	}
}
