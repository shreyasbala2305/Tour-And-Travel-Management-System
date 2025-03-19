package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/TourPackage.java

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tour_packages")
public class TourPackage {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @Column(nullable = false)
 private String name;
 
 private String description;
 
 @Column(nullable = false)
 private BigDecimal price;
 
 private int duration; // in days
 
 private String imageUrl;
 
 @Column(nullable = false)
 private int maxGroupSize;
 
 private boolean featured;
 
 @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
 private List<Itinerary> itineraries = new ArrayList<>();
 
 @ManyToOne
 @JoinColumn(name = "destination_id")
 private Destination destination;
 
 @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
 private List<Booking> bookings = new ArrayList<>();
}
