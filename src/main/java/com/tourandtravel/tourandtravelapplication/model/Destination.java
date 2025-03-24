package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/Destination.java

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "destinations")
@ToString(exclude = "tourPackages")
public class Destination {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @Column(nullable = false)
 private String name;
 
 private String description;
 
 private String country;
 
 private String imageUrl;
 
 private String mapLocation;
 
 private String weather;
 
 private String currency;
 
 private String bestTimeToVisit;
 
 private String language;
 
 private int attractionCount;
 
 private boolean popular;
 
 @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
 private List<TourPackage> tourPackages = new ArrayList<>();

public Destination orElse(Object object) {
	// TODO Auto-generated method stub
	return null;
}

}