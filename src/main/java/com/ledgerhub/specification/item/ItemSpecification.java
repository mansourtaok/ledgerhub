package com.ledgerhub.specification.item;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ledgerhub.model.db.items.Item;

public class ItemSpecification {

	public static Specification<Item> filterItems(String name, List<Integer> categoryIds) {
		return (root, query, cb) -> {

			var predicates = cb.conjunction();

			if (name != null && !name.isEmpty()) {
				predicates = cb.and(predicates, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
			}

			if (categoryIds != null && !categoryIds.isEmpty()) {
				predicates = cb.and(predicates, root.get("categoryId").in(categoryIds));
			}

			return predicates;
		};
	}
}
