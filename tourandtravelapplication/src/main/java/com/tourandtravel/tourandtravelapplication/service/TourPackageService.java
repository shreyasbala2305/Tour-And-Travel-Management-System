package com.tourandtravel.tourandtravelapplication.service;
//src/main/java/com/tourandtravel/service/TourPackageService.java

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourandtravel.tourandtravelapplication.model.TourPackage;
import com.tourandtravel.tourandtravelapplication.repository.TourPackageRepository;

@Service
public class TourPackageService {
 
 @Autowired
 private TourPackageRepository tourPackageRepository;
 
 public List<TourPackage> getAllTourPackages() {
     return tourPackageRepository.findAll();
 }
 
 public Optional<TourPackage> getTourPackageById(Long id) {
     return tourPackageRepository.findById(id);
 }
 
 public List<TourPackage> getFeaturedTourPackages() {
     return tourPackageRepository.findByFeaturedTrue();
 }
 
 public List<TourPackage> getTourPackagesByDestination(Long destinationId) {
     return tourPackageRepository.findByDestinationId(destinationId);
 }
 
 public TourPackage createTourPackage(TourPackage tourPackage) {
     return tourPackageRepository.save(tourPackage);
 }
 
 public TourPackage updateTourPackage(TourPackage tourPackage) {
     return tourPackageRepository.save(tourPackage);
 }
 
 public void deleteTourPackage(Long id) {
     tourPackageRepository.deleteById(id);
 }

 public List<TourPackage> searchTourPackages(String query) {
	    if (query == null || query.trim().isEmpty()) {
	        return new ArrayList<>();
	    }
	    
	    String searchQuery = "%" + query.toLowerCase() + "%";
	    
	    return tourPackageRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrDestinationContainingIgnoreCase(
	        searchQuery, searchQuery, searchQuery);
	}
}