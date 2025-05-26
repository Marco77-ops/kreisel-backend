package edu.hm.cs.kreisel_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubcategoryDto {
    private Long id;
    private String name;
    private Long categoryId;
}

