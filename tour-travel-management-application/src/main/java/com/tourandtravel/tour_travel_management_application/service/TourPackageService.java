package com.tourandtravel.tour_travel_management_application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.repositories.TourPackageRepository;

@Service
public class TourPackageService {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Transactional
    public TourPackage createTourPackage(TourPackage tourPackage) {
        // Additional validation can be added here
        validateTourPackage(tourPackage);
        return tourPackageRepository.save(tourPackage);
    }

    public List<TourPackage> getAllTourPackages() {
        return tourPackageRepository.findAll();
    }

    public TourPackage getTourPackageById(Long id) {
        return tourPackageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tour Package not found"));
    }

    public List<TourPackage> searchTourPackages(
        TourPackage.DifficultyLevel difficultyLevel,
        BigDecimal maxPrice,
        LocalDate startDate,
        LocalDate endDate
    ) {
        List<TourPackage> packages = tourPackageRepository.findAll();

        return packages.stream()
            .filter(pkg -> difficultyLevel == null || pkg.getDifficultyLevel() == difficultyLevel)
            .filter(pkg -> maxPrice == null || pkg.getPrice().compareTo(maxPrice) <= 0)
            .filter(pkg -> startDate == null || pkg.getStartDate().isAfter(startDate))
            .filter(pkg -> endDate == null || pkg.getEndDate().isBefore(endDate))
            .collect(Collectors.toList());
    }

    @Transactional
    public TourPackage updateTourPackage(Long id, TourPackage tourPackageDetails) {
        TourPackage existingPackage = getTourPackageById(id);
        
        // Update fields
        existingPackage.setPackageName(tourPackageDetails.getPackageName());
        existingPackage.setDescription(tourPackageDetails.getDescription());
        existingPackage.setPrice(tourPackageDetails.getPrice());
        existingPackage.setDuration(tourPackageDetails.getDuration());
        existingPackage.setDifficultyLevel(tourPackageDetails.getDifficultyLevel());
        existingPackage.setDestinations(tourPackageDetails.getDestinations());
        existingPackage.setStartDate(tourPackageDetails.getStartDate());
        existingPackage.setEndDate(tourPackageDetails.getEndDate());
        existingPackage.setMinParticipants(tourPackageDetails.getMinParticipants());
        existingPackage.setMaxParticipants(tourPackageDetails.getMaxParticipants());

        validateTourPackage(existingPackage);
        return tourPackageRepository.save(existingPackage);
    }

    @Transactional
    public void deleteTourPackage(Long id) {
        TourPackage tourPackage = getTourPackageById(id);
        tourPackageRepository.delete(tourPackage);
    }

    private void validateTourPackage(TourPackage tourPackage) {
        if (tourPackage.getStartDate().isAfter(tourPackage.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (tourPackage.getMinParticipants() > tourPackage.getMaxParticipants()) {
            throw new IllegalArgumentException("Minimum participants must be less than or equal to maximum participants");
        }

        if (tourPackage.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}
