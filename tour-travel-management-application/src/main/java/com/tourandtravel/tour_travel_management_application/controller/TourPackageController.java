package com.tourandtravel.tour_travel_management_application.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.service.TourPackageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tour-packages")
public class TourPackageController {

    @Autowired
    private TourPackageService tourPackageService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<TourPackage> createTourPackage(@Valid @RequestBody TourPackage tourPackage) {
        TourPackage createdPackage = tourPackageService.createTourPackage(tourPackage);
        return ResponseEntity.ok(createdPackage);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TourPackage>> getAllTourPackages() {
        List<TourPackage> packages = tourPackageService.getAllTourPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackage> getTourPackageById(@PathVariable Long id) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        return ResponseEntity.ok(tourPackage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourPackage>> searchTourPackages(
        @RequestParam(required = false) TourPackage.DifficultyLevel difficultyLevel,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate
    ) {
        List<TourPackage> packages = tourPackageService.searchTourPackages(
            difficultyLevel, maxPrice, startDate, endDate
        );
        return ResponseEntity.ok(packages);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<TourPackage> updateTourPackage(
        @PathVariable Long id, 
        @Valid @RequestBody TourPackage tourPackageDetails
    ) {
        TourPackage updatedPackage = tourPackageService.updateTourPackage(id, tourPackageDetails);
        return ResponseEntity.ok(updatedPackage);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTourPackage(@PathVariable Long id) {
        tourPackageService.deleteTourPackage(id);
        return ResponseEntity.ok().build();
    }
}
