package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/Itinerary.java

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "itineraries")
public class Itinerary {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @ManyToOne
 @JoinColumn(name = "tour_package_id")
 private TourPackage tourPackage;
 
 private int dayNumber;
 
 private String title;
 
 @Column(columnDefinition = "TEXT")
 private String description;
 
 private String location;
 
 private String activities;
 
 private String meals; // e.g., "Breakfast, Lunch"
 
 private String accommodation;
}