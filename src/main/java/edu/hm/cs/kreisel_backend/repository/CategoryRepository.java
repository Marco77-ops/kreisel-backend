package edu.hm.cs.kreisel_backend.repository;
import edu.hm.cs.kreisel_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
