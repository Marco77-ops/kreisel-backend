package edu.hm.cs.kreisel_backend.repository;

import edu.hm.cs.kreisel_backend.model.Item;
import edu.hm.cs.kreisel_backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Für Filter nach Location (über Location-Objekt)
    List<Item> findByLocation(Location location);

    // Optional: Für Suche nach Name, Description, Brand (Case Insensitive)
    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(
            String name, String description, String brand
    );
}
