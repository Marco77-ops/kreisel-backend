package edu.hm.cs.kreisel_backend.service;

import edu.hm.cs.kreisel_backend.dto.RentalCreateDto;
import edu.hm.cs.kreisel_backend.dto.RentalResponseDto;
import edu.hm.cs.kreisel_backend.mapper.RentalMapper;
import edu.hm.cs.kreisel_backend.model.Item;
import edu.hm.cs.kreisel_backend.model.Rental;
import edu.hm.cs.kreisel_backend.model.User;
import edu.hm.cs.kreisel_backend.repository.ItemRepository;
import edu.hm.cs.kreisel_backend.repository.RentalRepository;
import edu.hm.cs.kreisel_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {

    private static final int MAX_ACTIVE_RENTALS = 5;
    private static final int MAX_RENTAL_DAYS = 60;
    private static final int EXTENSION_DAYS = 30;

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalsByUser(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    public List<Rental> getActiveRentalsByUser(Long userId) {
        return rentalRepository.findByUserIdAndReturnDateIsNull(userId);
    }

    public List<Rental> getHistoricalRentalsByUser(Long userId) {
        return rentalRepository.findByUserIdAndReturnDateIsNotNull(userId);
    }

    public Optional<Rental> getActiveRentalForItem(Long itemId) {
        return rentalRepository.findByItemIdAndReturnDateIsNull(itemId);
    }

    /**
     * Prüft, ob das Item aktuell verliehen ist (returnDate == null).
     */
    private boolean isItemAvailable(Long itemId) {
        return rentalRepository.findByItemIdAndReturnDateIsNull(itemId).isEmpty();
    }

    public Rental rentItem(Long userId, Rental rentalRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long itemId = rentalRequest.getItem() != null ? rentalRequest.getItem().getId() : null;
        if (itemId == null) {
            throw new RuntimeException("Item ID ist erforderlich");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Verfügbarkeitsprüfung NUR über Rentals:
        if (!isItemAvailable(item.getId())) {
            throw new RuntimeException("Item ist nicht verfügbar");
        }

        // Prüfe max. 5 aktive Ausleihen für den User
        List<Rental> activeRentals = getActiveRentalsByUser(userId);
        if (activeRentals.size() >= MAX_ACTIVE_RENTALS) {
            throw new RuntimeException("Maximale Anzahl aktiver Ausleihen (" + MAX_ACTIVE_RENTALS + ") erreicht");
        }

        // Enddatum prüfen und setzen
        if (rentalRequest.getEndDate() == null) {
            throw new RuntimeException("Enddatum ist erforderlich");
        }
        LocalDate endDate = validateAndSetEndDate(rentalRequest.getEndDate());

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setItem(item);
        rental.setRentalDate(LocalDate.now());
        rental.setEndDate(endDate);
        rental.setReturnDate(null);
        rental.setExtended(false);

        return rentalRepository.save(rental);
    }

    private LocalDate validateAndSetEndDate(LocalDate requestedEndDate) {
        LocalDate today = LocalDate.now();
        LocalDate maxEndDate = today.plusDays(MAX_RENTAL_DAYS);

        if (requestedEndDate.isBefore(today)) {
            throw new RuntimeException("Enddatum darf nicht in der Vergangenheit liegen");
        }
        if (requestedEndDate.isAfter(maxEndDate)) {
            throw new RuntimeException("Enddatum darf maximal " + MAX_RENTAL_DAYS + " Tage in der Zukunft liegen");
        }
        if (requestedEndDate.isEqual(today)) {
            throw new RuntimeException("Ausleihdauer muss mindestens 1 Tag betragen");
        }

        return requestedEndDate;
    }

    public Rental extendRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new RuntimeException("Rental ist bereits zurückgegeben");
        }
        if (rental.isExtended()) {
            throw new RuntimeException("Verlängerung bereits genutzt");
        }

        LocalDate newEndDate = rental.getEndDate().plusDays(EXTENSION_DAYS);
        LocalDate maxAllowedDate = rental.getRentalDate().plusDays(MAX_RENTAL_DAYS + EXTENSION_DAYS);

        if (newEndDate.isAfter(maxAllowedDate)) {
            throw new RuntimeException("Verlängerung würde die maximale Ausleihdauer überschreiten");
        }

        rental.setEndDate(newEndDate);
        rental.setExtended(true);

        return rentalRepository.save(rental);
    }

    public Rental returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new RuntimeException("Rental ist bereits zurückgegeben");
        }

        rental.setReturnDate(LocalDate.now());
        return rentalRepository.save(rental);
    }

    public List<Rental> getOverdueRentals() {
        return rentalRepository.findAll().stream()
                .filter(rental -> rental.getReturnDate() == null)
                .filter(rental -> rental.getEndDate().isBefore(LocalDate.now()))
                .toList();
    }

    public List<RentalResponseDto> getAllRentalDtos() {
        return rentalRepository.findAll().stream()
                .map(RentalMapper::toDto)
                .toList();
    }

    public List<RentalResponseDto> getRentalDtosByUser(Long userId) {
        return rentalRepository.findByUserId(userId).stream()
                .map(RentalMapper::toDto)
                .toList();
    }

    public RentalResponseDto rentItemDto(Long userId, RentalCreateDto dto) {
        Rental request = new Rental();
        // Du solltest besser im Service eine sauberere Factory bauen, das hier reicht für den Start:
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        request.setItem(item);
        request.setEndDate(dto.getEndDate());
        Rental saved = rentItem(userId, request); // nutzt deine bestehende Logik!
        return RentalMapper.toDto(saved);
    }

// Analog für extendRentalDto(), returnRentalDto() etc.

}
