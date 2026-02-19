package com.ledgerhub.service.items.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ledgerhub.model.db.items.Item;
import com.ledgerhub.model.dto.items.ItemRequestDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;
import com.ledgerhub.repository.ItemRepository;
import com.ledgerhub.service.items.IItemService;
import com.ledgerhub.specification.item.ItemSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService implements IItemService {

	private final ItemRepository itemRepository;

	@Override
	public ItemResponseDTO create(ItemRequestDTO dto) {

		Item item = Item.builder().companyId(dto.getCompanyId()).categoryId(dto.getCategoryId())
				.currencyId(dto.getCurrencyId()).name(dto.getName()).sku(dto.getSku()).description(dto.getDescription())
				.costPrice(dto.getCostPrice()).sellingPrice(dto.getSellingPrice()).createdAt(LocalDateTime.now())
				.stockQtyNotify(dto.getStockQtyNotify()).stockQuantity(dto.getStockQuantity()).build();

		item = itemRepository.save(item);

		return mapToResponse(item);
	}

	@Override
	public Page<ItemResponseDTO> getAll(String name, List<Integer> categoryIds, int page, int size, String[] sort) {

		List<Sort.Order> orders = new ArrayList<>();
		
		if (sort == null || sort.length == 0) {
			sort = new String[] { "name,desc" };
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

		var spec = ItemSpecification.filterItems(name, categoryIds);

		return itemRepository.findAll(spec, pageable).map(this::mapToResponse);
	}

	@Override
	public ItemResponseDTO getById(Long id) {
		Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
		return mapToResponse(item);
	}

	@Override
	public ItemResponseDTO update(Long id, ItemRequestDTO dto) {

		Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

		item.setName(dto.getName());
		item.setSku(dto.getSku());
		item.setCategoryId(dto.getCategoryId());
		item.setSellingPrice(dto.getSellingPrice());
		item.setCostPrice(dto.getCostPrice());
		item.setLastUpdateAt(LocalDateTime.now());

		item = itemRepository.save(item);

		return mapToResponse(item);
	}

	@Override
	public void delete(Long id) {
		itemRepository.deleteById(id);
	}

	private ItemResponseDTO mapToResponse(Item item) {
		return ItemResponseDTO.builder().id(item.getId()).name(item.getName()).sku(item.getSku())
				.categoryId(item.getCategoryId()).sellingPrice(item.getSellingPrice())
				.stockQuantity(item.getStockQuantity()).status(item.getStatus()).build();
	}
}
