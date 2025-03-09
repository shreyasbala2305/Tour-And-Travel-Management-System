package com.tourandtravel.tour_travel_management_application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tourandtravel.tour_travel_management_application.model.Review;
import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.model.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByTourPackage(TourPackage tourPackage);
    List<Review> findByUser(User user);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tourPackage = :tourPackage")
    Double getAverageRatingForTourPackage(TourPackage tourPackage);
}
