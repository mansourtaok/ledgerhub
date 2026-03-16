package com.ledgerhub.service.expenses;

import java.util.List;

import com.ledgerhub.model.dto.expenses.ExpenseTypeRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseTypeResponseDTO;

public interface IExpenseTypeService {

	List<ExpenseTypeResponseDTO> findAll(Long companyId);

	ExpenseTypeResponseDTO create(ExpenseTypeRequestDTO dto, Long companyId);

	ExpenseTypeResponseDTO update(Long id, ExpenseTypeRequestDTO dto);

	void delete(Long id);

}
