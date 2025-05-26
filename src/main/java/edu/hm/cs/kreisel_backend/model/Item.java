package edu.hm.cs.kreisel_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String size;

    private String description;

    private String brand;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    // Gender als Enum, du kannst hier bei wenigen, stabilen Werten erstmal dabei bleiben.
    public enum Gender {
        DAMEN, HERREN, UNISEX
    }
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    // Zustand als Enum, für 2-3 feste Werte ausreichend
    public enum Zustand {
        NEU, GEBRAUCHT
    }
    @Enumerated(EnumType.STRING)
    private Zustand zustand;

    // Entfernt: available-Flag, weil du es per Rental ableitest!

    // Standard-Konstruktor und ggf. Convenience-Konstruktoren...
}
