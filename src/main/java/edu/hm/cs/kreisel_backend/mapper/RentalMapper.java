package edu.hm.cs.kreisel_backend.mapper;

import edu.hm.cs.kreisel_backend.dto.RentalResponseDto;
import edu.hm.cs.kreisel_backend.model.Rental;

public class RentalMapper {
    public static RentalResponseDto toDto(Rental rental) {
        return RentalResponseDto.builder()
                .id(rental.getId())
                .userId(rental.getUser() != null ? rental.getUser().getId() : null)
                .userName(rental.getUser() != null ? rental.getUser().getFullName() : null)
                .itemId(rental.getItem() != null ? rental.getItem().getId() : null)
                .itemName(rental.getItem() != null ? rental.getItem().getName() : null)
                .rentalDate(rental.getRentalDate())
                .endDate(rental.getEndDate())
                .returnDate(rental.getReturnDate())
                .extended(rental.isExtended())
                .build();
    }
}
