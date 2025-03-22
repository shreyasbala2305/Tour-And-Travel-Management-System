package com.tourandtravel.tourandtravelapplication.repository;
//src/main/java/com/tourandtravel/repository/DestinationRepository.java

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourandtravel.tourandtravelapplication.model.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
 List<Destination> findByPopularTrue();
 List<Destination> findByCountry(String country);
 Destination findByName(String name);
}