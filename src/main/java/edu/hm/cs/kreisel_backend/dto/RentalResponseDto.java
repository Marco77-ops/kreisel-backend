package edu.hm.cs.kreisel_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class RentalResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long itemId;
    private String itemName;
    private LocalDate rentalDate;
    private LocalDate endDate;
    private LocalDate returnDate;
    private boolean extended;
}
