package com.ledgerhub.utils;

import org.springframework.data.jpa.domain.Specification;

import com.ledgerhub.model.db.Profile;
import com.ledgerhub.model.db.Staff;

public class ProfileSpecification {

	public static Specification<Profile> fullNameLike(String name) {
		return (root, query, cb) -> name == null ? null
				: cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

	public static Specification<Profile> categoryEquals(Long categoryId) {
		return (root, query, cb) -> categoryId == null ? null
				: cb.equal(root.get("categoryId").get("id"), categoryId);
	}

	public static Specification<Profile> typeEquals(Long typeId) {
		return (root, query, cb) -> typeId == null ? null
				: cb.equal(root.get("typeId").get("id"), typeId);
	}
	
	public static Specification<Staff> jobDescriptionEquals(Long jobDescriptionId) {
		return (root, query, cb) -> jobDescriptionId == null ? null
				: cb.equal(root.get("jobDescription").get("id"), jobDescriptionId);
	}
}
