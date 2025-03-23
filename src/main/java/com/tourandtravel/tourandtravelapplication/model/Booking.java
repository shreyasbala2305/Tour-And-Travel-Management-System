package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/Booking.java

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @ManyToOne
 @JoinColumn(name = "user_id")
 private User user;
 
 @ManyToOne
 @JoinColumn(name = "tour_package_id")
 private TourPackage tourPackage;
 
 private LocalDate bookingDate;
 
 private String bookingReference;
 
 private LocalDate travelDate;
 
 private int numberOfPeople;
 
 private BigDecimal totalAmount;
 
 @Enumerated(EnumType.STRING)
 private BookingStatus status;
 
 @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
 private Payment payment;
 
 public enum BookingStatus {
     PENDING, CONFIRMED, CANCELLED, COMPLETED
 }
}