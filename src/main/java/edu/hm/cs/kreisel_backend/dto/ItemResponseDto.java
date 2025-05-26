package edu.hm.cs.kreisel_backend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDto {
    private Long id;
    private String name;
    private String size;
    private String description;
    private String brand;
    private LocationDto location;
    private String gender;
    private CategoryDto category;
    private SubcategoryDto subcategory;
    private String zustand;
}
