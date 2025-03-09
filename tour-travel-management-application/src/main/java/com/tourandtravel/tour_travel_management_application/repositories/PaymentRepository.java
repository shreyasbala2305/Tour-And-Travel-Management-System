package com.tourandtravel.tour_travel_management_application.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.tourandtravel.tour_travel_management_application.model.Booking;
import com.tourandtravel.tour_travel_management_application.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	List<Payment> findByStatus(Payment.PaymentStatus status);
    Optional<Payment> findByBooking(Booking booking);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    java.math.BigDecimal getTotalRevenueByStatus(Payment.PaymentStatus status);
}
