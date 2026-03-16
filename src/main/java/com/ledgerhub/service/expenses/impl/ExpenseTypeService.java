package com.ledgerhub.service.expenses.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.expenses.ExpenseType;
import com.ledgerhub.model.dto.expenses.ExpenseTypeRequestDTO;
import com.ledgerhub.model.dto.expenses.ExpenseTypeResponseDTO;
import com.ledgerhub.repository.ExpenseTypeRepository;
import com.ledgerhub.service.expenses.IExpenseTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseTypeService implements IExpenseTypeService {

	private final ExpenseTypeRepository repository;

	@Override
	public ExpenseTypeResponseDTO create(ExpenseTypeRequestDTO dto, Long companyId) {
		ExpenseType expType = new ExpenseType();
				
		expType.setCompanyId(companyId);
		expType.setExp_type_label(dto.getExpTypeLabel());
		expType.setActive(dto.getActive());
		return mapToDTO(repository.save(expType));
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new IllegalArgumentException("Expense Type not found");
		}//TODO Check usage of id
		repository.deleteById(id);
	}

	@Override
	public ExpenseTypeResponseDTO update(Long id, ExpenseTypeRequestDTO dto) {

		ExpenseType category = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Expense Type not found"));

		category.setExp_type_label(dto.getExpTypeLabel());
		category.setActive(dto.getActive());

		return mapToDTO(repository.save(category));
	}

	@Override
	public List<ExpenseTypeResponseDTO> findAll(Long companyId) {

		return repository.findByCompanyId(companyId).stream().map(x->mapToDTO(x)).collect(Collectors.toList());
	}

	/* Mapping */

	private ExpenseTypeResponseDTO mapToDTO(ExpenseType s) {
		return ExpenseTypeResponseDTO.builder().id(s.getId()).expTypeLabel(s.getExp_type_label())
				.active(s.getActive()).build();
	}
}
