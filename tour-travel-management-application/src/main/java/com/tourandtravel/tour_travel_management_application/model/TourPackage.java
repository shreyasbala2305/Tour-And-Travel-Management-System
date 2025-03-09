package com.tourandtravel.tour_travel_management_application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tour_packages")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Package name is required")
    private String packageName;

    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private int duration; // in days

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ElementCollection
    private Set<String> destinations;

    private LocalDate startDate;
    private LocalDate endDate;

    @Positive(message = "Minimum participants must be positive")
    private int minParticipants;

    @Positive(message = "Maximum participants must be positive")
    private int maxParticipants;

    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    // Enum for Difficulty Levels
    public enum DifficultyLevel {
        EASY,
        MODERATE,
        CHALLENGING,
        EXTREME
    }

    // Utility method to check availability
    public boolean isAvailable() {
        return bookings == null || bookings.size() < maxParticipants;
    }
}
