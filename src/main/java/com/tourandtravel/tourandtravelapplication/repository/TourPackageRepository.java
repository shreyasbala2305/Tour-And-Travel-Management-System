package com.tourandtravel.tourandtravelapplication.repository;
//src/main/java/com/tourandtravel/repository/TourPackageRepository.java

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourandtravel.tourandtravelapplication.model.TourPackage;

public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
List<TourPackage> findByFeaturedTrue();
List<TourPackage> findByDestinationId(Long destinationId);
List<TourPackage> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrDestination_NameContainingIgnoreCase(
        String name, String description, String destinationName);
}