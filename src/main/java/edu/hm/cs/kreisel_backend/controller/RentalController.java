package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.dto.RentalCreateDto;
import edu.hm.cs.kreisel_backend.dto.RentalResponseDto;
import edu.hm.cs.kreisel_backend.model.Rental;
import edu.hm.cs.kreisel_backend.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<RentalResponseDto>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentalDtos());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalResponseDto>> getRentalsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.getRentalDtosByUser(userId));
    }

    @PostMapping("/user/{userId}/rent")
    public ResponseEntity<RentalResponseDto> rentItem(@PathVariable Long userId,
                                                      @RequestBody RentalCreateDto rentalRequest) {
        return ResponseEntity.ok(rentalService.rentItemDto(userId, rentalRequest));
    }
    // Ergänze analog extendRentalDto() und returnRentalDto()
}

