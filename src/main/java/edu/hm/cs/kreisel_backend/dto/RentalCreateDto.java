package edu.hm.cs.kreisel_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RentalCreateDto {
    private Long itemId;
    private LocalDate endDate;
}
