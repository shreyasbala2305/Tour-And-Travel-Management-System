package com.tourandtravel.tourandtravelapplication.repository;
//src/main/java/com/tourandtravel/repository/BookingRepository.java

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourandtravel.tourandtravelapplication.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
 List<Booking> findByUserId(Long userId);
 List<Booking> findByTourPackageId(Long tourPackageId);
}