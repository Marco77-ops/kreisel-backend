package edu.hm.cs.kreisel_backend.mapper;

import edu.hm.cs.kreisel_backend.dto.CategoryDto;
import edu.hm.cs.kreisel_backend.dto.ItemResponseDto;
import edu.hm.cs.kreisel_backend.dto.LocationDto;
import edu.hm.cs.kreisel_backend.dto.SubcategoryDto;
import edu.hm.cs.kreisel_backend.model.Item;

public class ItemMapper {
    public static ItemResponseDto toDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setSize(item.getSize());
        dto.setDescription(item.getDescription());
        dto.setBrand(item.getBrand());
        dto.setGender(item.getGender() != null ? item.getGender().name() : null);
        dto.setZustand(item.getZustand() != null ? item.getZustand().name() : null);
        // Location
        LocationDto loc = new LocationDto();
        loc.setId(item.getLocation().getId());
        loc.setName(item.getLocation().getName());
        loc.setAddress(item.getLocation().getAddress());
        dto.setLocation(loc);
        // Category
        CategoryDto cat = new CategoryDto();
        cat.setId(item.getCategory().getId());
        cat.setName(item.getCategory().getName());
        dto.setCategory(cat);
        // Subcategory
        SubcategoryDto sub = new SubcategoryDto();
        sub.setId(item.getSubcategory().getId());
        sub.setName(item.getSubcategory().getName());
        sub.setCategoryId(item.getSubcategory().getCategory().getId());
        dto.setSubcategory(sub);

        return dto;
    }
}

