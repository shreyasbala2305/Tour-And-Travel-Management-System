package com.tourandtravel.tour_travel_management_application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Booking must have a user")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Booking must have a tour package")
    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private TourPackage tourPackage;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private int numberOfParticipants;

    private BigDecimal totalPrice;

    @CreationTimestamp
    private LocalDateTime bookingDate;

    private LocalDateTime paymentDate;

    private String paymentTransactionId;

    // Booking Status Enum
    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }

    // Utility method to calculate total price
    public void calculateTotalPrice() {
        if (tourPackage != null) {
            this.totalPrice = tourPackage.getPrice().multiply(BigDecimal.valueOf(numberOfParticipants));
        }
    }
}
