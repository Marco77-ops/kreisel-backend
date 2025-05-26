package edu.hm.cs.kreisel_backend.repository;
import edu.hm.cs.kreisel_backend.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findByCategory_Id(Long categoryId);
    boolean existsByName(String name);
}
