package com.tourandtravel.tour_travel_management_application.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tourandtravel.tour_travel_management_application.model.Booking;
import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.model.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
	List<Booking> findByUser(User user);
    List<Booking> findByTourPackage(TourPackage tourPackage);
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
