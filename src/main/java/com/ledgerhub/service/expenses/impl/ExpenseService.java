package com.ledgerhub.service.expenses.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.expenses.Expense;
import com.ledgerhub.model.dto.expenses.ExpenseRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseResponseDTO;
import com.ledgerhub.repository.ExpenseRepository;
import com.ledgerhub.service.expenses.IExpenseService;
import com.ledgerhub.specification.expense.ExpenseSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService implements IExpenseService {

	private final ExpenseRepository repository;

	@Override
	public ExpenseResponseDTO create(ExpenseRequestDTO dto, Long companyId) {
		Expense expense = new Expense();
				
		expense.setCompanyId(companyId);
		expense.setExpTypeId(dto.getExpTypeId());
		expense.setDate(dto.getDate());
		expense.setAmount(dto.getAmount());
		expense.setCurrency(dto.getCurrency());
		expense.setPayment_method(dto.getPaymentMethod());
		expense.setNotes(dto.getNotes());
		return mapToDTO(repository.save(expense));
	}

	@Override
	public Page<ExpenseResponseDTO> getAll(String notes, List<Integer> expTypeIds, int page, int size, String[] sort) {
		
		List<Sort.Order> orders = new ArrayList<>();
		
		if (sort == null || sort.length == 0) {
			sort = new String[] { "date,desc" };
		}
		for (String s : sort) {
			String[] parts = s.split(",", 2); // split into [property, direction]
			String property = parts[0].trim();
			Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC
					: Sort.Direction.ASC;
			orders.add(new Sort.Order(direction, property));
		}

		Sort sorting = Sort.by(orders);

		Pageable pageable = PageRequest.of(page, size, sorting);

		var spec = ExpenseSpecification.filterExpenses(notes, expTypeIds);

		return repository.findAll(spec, pageable).map(this::mapToDTO);

	}
	
	@Override
	public ExpenseResponseDTO getById(Long id) {
		Expense expense = repository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
		return mapToDTO(expense);
	}

	@Override
	public ExpenseResponseDTO update(Long id, ExpenseRequestDTO dto) {

		Expense expense = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Expense not found"));

		expense.setExpTypeId(dto.getExpTypeId());
		expense.setDate(dto.getDate());
		expense.setAmount(dto.getAmount());
		expense.setCurrency(dto.getCurrency());
		expense.setPayment_method(dto.getPaymentMethod());
		expense.setNotes(dto.getNotes());
		return mapToDTO(repository.save(expense));
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new IllegalArgumentException("Expense not found");
		}//TODO Check usage of id
		repository.deleteById(id);
	}
/*
	private ExpenseResponseDTO mapToResponse(Expense item) {
		return ExpenseResponseDTO.builder().id(item.getId()).name(item.getName()).sku(item.getSku())
				.categoryId(item.getCategoryId()).sellingPrice(item.getSellingPrice())
				.stockQuantity(item.getStockQuantity()).status(item.getStatus()).build();
	}*/
	
	/* Mapping */

	private ExpenseResponseDTO mapToDTO(Expense s) {
		return ExpenseResponseDTO.builder().id(s.getId()).expTypeId(s.getExpTypeId()).date(s.getDate())
				.amount(s.getAmount()).currencyId(s.getCurrency())
				.payment_method(s.getPayment_method()).notes(s.getNotes()).build();
	}
}
