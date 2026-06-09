package com.ledgerhub.service.items.impl;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ledgerhub.config.jwt.JwtTokenUtil;
import com.ledgerhub.model.db.BaseEntity;
import com.ledgerhub.model.db.Document;
import com.ledgerhub.model.db.items.Item;
import com.ledgerhub.model.dto.items.ItemRequestDTO;
import com.ledgerhub.model.dto.items.ItemResponseDTO;
import com.ledgerhub.repository.DocumentRepository;
import com.ledgerhub.repository.EntityRepository;
import com.ledgerhub.repository.ItemCategoryRepository;
import com.ledgerhub.repository.ItemRepository;
import com.ledgerhub.repository.SystemLookupRepository;
import com.ledgerhub.service.items.IItemService;
import com.ledgerhub.specification.item.ItemSpecification;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService implements IItemService {

	private final ItemRepository itemRepository;
	private final EntityRepository entityRepository;
	private final SystemLookupRepository systemLookupRepository;
	private final ItemCategoryRepository itemCategoryRepository;
	private final DocumentRepository documentRepository;
	private final JwtTokenUtil jwtTokenUtil;

	// TODO change to app properties
	private static final String uploadDir = "C:\\Users\\User\\Documents\\Mansour\\projects\\ledgerhub";

	@Transactional
	@Override
	public ItemResponseDTO create(ItemRequestDTO dto) {

		BaseEntity entity = BaseEntity.builder().entityType("ITEM").build();
		entity = entityRepository.save(entity);

		List<String> documentPaths = new ArrayList<>();
		MultipartFile[] files = dto.getFiles();
		if (files != null) {
			for (MultipartFile file : files) {

				String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
				Path path = Paths.get(uploadDir, filename);

				try {
					Files.createDirectories(path.getParent());
					Files.write(path, file.getBytes());
				} catch (Exception e) {
					throw new RuntimeException("File upload failed");
				}

				Document doc = new Document();
				doc.setFilename(file.getOriginalFilename());
				doc.setFilepath(path.toString());
				doc.setReferenceId(entity.getId());

				documentRepository.save(doc);

				documentPaths.add(path.toString());
			}
		}

		Item item = Item.builder().companyId(dto.getCompanyId()).categoryId(dto.getCategoryId())
				.currencyId(dto.getCurrencyId()).name(dto.getName()).sku(dto.getSku()).description(dto.getDescription())
				.costPrice(dto.getCostPrice()).sellingPrice(dto.getSellingPrice()).createdAt(LocalDateTime.now())
				.stockQtyNotify(dto.getStockQtyNotify()).stockQuantity(dto.getStockQuantity()).entity(entity).build();

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

	@Override
	public void importFromExcel(Long companyId, MultipartFile file, HttpServletRequest request) {

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);
			List<Item> items = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Item item = new Item();

				LocalDateTime now = LocalDateTime.now();
				// Mandatory System Fields
				item.setCompanyId(companyId);
				item.setCreatedAt(now);
				item.setLastUpdateAt(now);

				Long userId = jwtTokenUtil.getUserIdFromToken(request.getHeader("Authorization"));
				item.setCreatedUserid(userId);
				item.setLastUpdateUserid(userId);

				// Basic Fields
				item.setName(getString(row, 0));
				item.setSku(getString(row, 1));

				String categoryLabel = getString(row, 2);
				Long categoryId = itemCategoryRepository.findByCompanyIdAndLabel(companyId, categoryLabel)
						.orElseThrow(() -> new RuntimeException("Invalid Category: " + categoryLabel)).getId();
				item.setCategoryId(categoryId);

				// Currency lookup by code
				String currencyCode = getString(row, 3);
				Long currencyId = systemLookupRepository.findByCategoryAndCode("CURRENCY", currencyCode)
						.orElseThrow(() -> new RuntimeException("Invalid currency code: " + currencyCode)).getId();
				item.setCurrencyId(currencyId);

				item.setDescription(getString(row, 4));
				item.setUnit(getString(row, 5));
				item.setReturnableStock(getBoolean(row, 6));
				item.setStockQtyNotify(getBigDecimal(row, 7));
				item.setDimension(getString(row, 8));
				item.setDimensionMeasure(getString(row, 9));
				item.setWeight(getBigDecimal(row, 10));
				item.setPackaging(getString(row, 11));
				item.setManufacturer(getString(row, 12));
				item.setBrand(getString(row, 13));
				item.setCostPrice(getBigDecimal(row, 14));
				item.setPurchaseAccount(getString(row, 15));
				item.setSellingPrice(getBigDecimal(row, 16));
				item.setSellingAccount(getString(row, 17));
				item.setNotes(getString(row, 18));
				item.setTaxable(getBoolean(row, 19));
				item.setTaxPercent(getBigDecimal(row, 20));
				item.setStockQuantity(getBigDecimal(row, 21));
				item.setHaveExpiryDate(getBoolean(row, 22));
				item.setLotNumber(getString(row, 23));
				item.setExpiryDate(getLocalDate(row, 24));
				item.setOnlineSale(getBoolean(row, 25));
				item.setStatus(getBoolean(row, 26));

				items.add(item);

			}

			itemRepository.saveAll(items);

		} catch (Exception e) {
			throw new RuntimeException("Error importing items: " + e.getMessage());
		}
	}

	private String getString(Row row, int index) {
		Cell cell = row.getCell(index);
		return (cell == null) ? null : cell.toString().trim();
	}

	private BigDecimal getBigDecimal(Row row, int index) {
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;
		return BigDecimal.valueOf(cell.getNumericCellValue());
	}

	private Boolean getBoolean(Row row, int index) {
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;
		return cell.getBooleanCellValue();
	}

	private LocalDate getLocalDate(Row row, int index) {
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;

		if (cell.getCellType() == CellType.NUMERIC) {
			return cell.getLocalDateTimeCellValue().toLocalDate();
		}

		return null;
	}

	private ItemResponseDTO mapToResponse(Item item) {
		List<String> documents = documentRepository.findByReferenceId(item.getId()).stream()
				.map(Document::getFilepath).toList();
		return ItemResponseDTO.builder().id(item.getId()).name(item.getName()).sku(item.getSku())
				.categoryId(item.getCategoryId()).sellingPrice(item.getSellingPrice())
				.stockQuantity(item.getStockQuantity()).status(item.getStatus()).documents(documents).build();
	}
}
