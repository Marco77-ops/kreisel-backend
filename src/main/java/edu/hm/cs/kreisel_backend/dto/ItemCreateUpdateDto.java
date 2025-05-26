package edu.hm.cs.kreisel_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateUpdateDto {
    private String name;
    private String size;
    private String description;
    private String brand;
    private Long locationId;
    private String gender;
    private Long categoryId;
    private Long subcategoryId;
    private String zustand;
}

