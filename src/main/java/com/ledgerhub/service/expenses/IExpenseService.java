package com.ledgerhub.service.expenses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ledgerhub.model.dto.expenses.ExpenseRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseResponseDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;
import com.ledgerhub.specification.item.ItemSpecification;

public interface IExpenseService {

	Page<ExpenseResponseDTO> getAll(String currency, List<Integer> expTypeIds, int page, int size, String[] sort);

	ExpenseResponseDTO getById(Long id);

	ExpenseResponseDTO create(ExpenseRequestDTO dto, Long companyId);

	ExpenseResponseDTO update(Long id, ExpenseRequestDTO dto);

	void delete(Long id);
	
	
}
