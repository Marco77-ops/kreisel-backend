package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.dto.ItemCreateUpdateDto;
import edu.hm.cs.kreisel_backend.dto.ItemResponseDto;
import edu.hm.cs.kreisel_backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * Liefert alle Items, unterstützt aber flexible Filter:
     * - locationId (Long): Filtert auf einen Standort
     * - categoryId (Long): Filtert auf eine Kategorie
     * - subcategoryId (Long): Filtert auf eine Unterkategorie
     * - gender (String): Filtert auf das gewünschte Geschlecht
     * - searchQuery (String): Volltextsuche Name/Beschreibung/Brand
     * - available (Boolean): true = nur verfügbare Items, false = nur aktuell ausgeliehene
     */
    @GetMapping
    public List<ItemResponseDto> getFilteredItems(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) Boolean available
    ) {
        return itemService.getFilteredItems(locationId, categoryId, subcategoryId, gender, searchQuery, available);
    }

    @GetMapping("/{id}")
    public ItemResponseDto getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public ItemResponseDto createItem(@RequestBody ItemCreateUpdateDto dto) {
        return itemService.createItem(dto);
    }

    @PutMapping("/{id}")
    public ItemResponseDto updateItem(@PathVariable Long id, @RequestBody ItemCreateUpdateDto dto) {
        return itemService.updateItem(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
