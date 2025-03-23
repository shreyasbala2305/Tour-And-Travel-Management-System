package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/TourPackage.java

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

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
 
 private LocalDate startDate;
 
 private String imageUrl;
 
 private String inclusions;
 
 private String exclusions;
 
 @Column(nullable = false)
 private int maxGroupSize;
 
 private boolean featured;
	
 @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
 private List<Itinerary> itineraries = new ArrayList<>();
 
 @Transient
 private String itinerariesText;
 
 public String getItinerariesText() {
    if (itineraries == null || itineraries.isEmpty()) {
        return "";
    }
    
    return itineraries.stream()
            .map(i -> "Day " + i.getDayNumber() + ": " + i.getTitle() + "\n" + 
                      i.getDescription() + "\n" + 
                      "Location: " + i.getLocation() + "\n" + 
                      "Activities: " + i.getActivities() + "\n" + 
                      "Meals: " + i.getMeals() + "\n" + 
                      "Accommodation: " + i.getAccommodation())
            .collect(Collectors.joining("\n\n"));
}

public void setItinerariesText(String text) {
    // This will be used to parse and create itineraries
    this.itinerariesText = text;
}
 
 @ManyToOne
 @JoinColumn(name = "destination_id")
 private Destination destination;
 
 @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
 private List<Booking> bookings = new ArrayList<>();
}
