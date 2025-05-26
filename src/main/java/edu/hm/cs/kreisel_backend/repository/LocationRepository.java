package edu.hm.cs.kreisel_backend.repository;
import edu.hm.cs.kreisel_backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByName(String name);
}
