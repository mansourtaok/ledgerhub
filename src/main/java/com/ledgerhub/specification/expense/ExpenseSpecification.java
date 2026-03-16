package com.ledgerhub.specification.expense;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ledgerhub.model.db.expenses.Expense;

public class ExpenseSpecification {

	public static Specification<Expense> filterExpenses(String notes, List<Integer> expTypeIds) {
		return (root, query, cb) -> {

			var predicates = cb.conjunction();

			if (notes != null && !notes.isEmpty()) {
				predicates = cb.and(predicates, cb.like(cb.lower(root.get("notes")), "%" + notes.toLowerCase() + "%"));
			}

			if (expTypeIds != null && !expTypeIds.isEmpty()) {
				predicates = cb.and(predicates, root.get("expTypeId").in(expTypeIds));
			}

			return predicates;
		};
	}
}
