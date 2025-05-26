package edu.hm.cs.kreisel_backend.service;

import edu.hm.cs.kreisel_backend.dto.ItemCreateUpdateDto;
import edu.hm.cs.kreisel_backend.dto.ItemResponseDto;
import edu.hm.cs.kreisel_backend.mapper.ItemMapper;
import edu.hm.cs.kreisel_backend.model.Item;
import edu.hm.cs.kreisel_backend.repository.CategoryRepository;
import edu.hm.cs.kreisel_backend.repository.ItemRepository;
import edu.hm.cs.kreisel_backend.repository.LocationRepository;
import edu.hm.cs.kreisel_backend.repository.SubcategoryRepository;
import edu.hm.cs.kreisel_backend.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final RentalRepository rentalRepository;

    public ItemResponseDto createItem(ItemCreateUpdateDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setSize(dto.getSize());
        item.setDescription(dto.getDescription());
        item.setBrand(dto.getBrand());
        item.setGender(Item.Gender.valueOf(dto.getGender()));
        item.setZustand(Item.Zustand.valueOf(dto.getZustand()));

        // FK-Referenzen setzen
        item.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found")));
        item.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        item.setSubcategory(subcategoryRepository.findById(dto.getSubcategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found")));

        Item saved = itemRepository.save(item);
        return ItemMapper.toDto(saved);
    }

    public ItemResponseDto updateItem(Long id, ItemCreateUpdateDto dto) {
        Item existing = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        existing.setName(dto.getName());
        existing.setSize(dto.getSize());
        existing.setDescription(dto.getDescription());
        existing.setBrand(dto.getBrand());
        existing.setGender(Item.Gender.valueOf(dto.getGender()));
        existing.setZustand(Item.Zustand.valueOf(dto.getZustand()));
        existing.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found")));
        existing.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        existing.setSubcategory(subcategoryRepository.findById(dto.getSubcategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found")));
        Item saved = itemRepository.save(existing);
        return ItemMapper.toDto(saved);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public ItemResponseDto getItemById(Long id) {
        return itemRepository.findById(id)
                .map(ItemMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    /**
     * Liefert alle Items, filterbar nach Location, Category, Subcategory, Gender, Suchtext, Verfügbarkeit.
     * Für große Datenmengen sollte man später QueryDSL, Criteria oder native Queries nutzen.
     */
    public List<ItemResponseDto> getFilteredItems(
            Long locationId,
            Long categoryId,
            Long subcategoryId,
            String gender,
            String searchQuery,
            Boolean available // true=nur verfügbare, false=nur ausgeliehene, null=alle
    ) {
        List<Item> items = itemRepository.findAll();

        return items.stream()
                .filter(item -> locationId == null || item.getLocation().getId().equals(locationId))
                .filter(item -> categoryId == null || item.getCategory().getId().equals(categoryId))
                .filter(item -> subcategoryId == null || item.getSubcategory().getId().equals(subcategoryId))
                .filter(item -> gender == null || item.getGender().name().equalsIgnoreCase(gender))
                .filter(item -> searchQuery == null ||
                        (item.getName() != null && item.getName().toLowerCase().contains(searchQuery.toLowerCase())) ||
                        (item.getBrand() != null && item.getBrand().toLowerCase().contains(searchQuery.toLowerCase())) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchQuery.toLowerCase()))
                )
                .filter(item -> available == null ||
                        (available && isItemAvailable(item)) ||
                        (!available && !isItemAvailable(item))
                )
                .map(ItemMapper::toDto)
                .toList();
    }

    /**
     * Ein Item ist verfügbar, wenn KEIN aktives Rental existiert (returnDate==null)
     */
    private boolean isItemAvailable(Item item) {
        return rentalRepository.findByItemIdAndReturnDateIsNull(item.getId()).isEmpty();
    }

    // Optional: getAllItems() kann als Convenience Methode für un-gefilterte Aufrufe erhalten bleiben
    public List<ItemResponseDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
