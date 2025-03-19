package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/Destination.java

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "destinations")
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
 
 private boolean popular;
 
 @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
 private List<TourPackage> tourPackages = new ArrayList<>();

public Destination orElse(Object object) {
	// TODO Auto-generated method stub
	return null;
}
}