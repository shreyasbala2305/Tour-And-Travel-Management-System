package com.tourandtravel.tourandtravelapplication.repository;
//src/main/java/com/tourandtravel/repository/PaymentRepository.java

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourandtravel.tourandtravelapplication.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
 Optional<Payment> findByBookingId(Long bookingId);
 Optional<Payment> findByTransactionId(String transactionId);
}