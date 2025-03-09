package com.tourandtravel.tour_travel_management_application.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tourandtravel.tour_travel_management_application.model.TourPackage;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long>{
List<TourPackage> findByDifficultyLevel(TourPackage.DifficultyLevel difficultyLevel);
    
    @Query("SELECT tp FROM TourPackage tp WHERE tp.startDate BETWEEN :startDate AND :endDate")
    List<TourPackage> findPackagesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<TourPackage> findByPriceLessThan(java.math.BigDecimal price);
}
