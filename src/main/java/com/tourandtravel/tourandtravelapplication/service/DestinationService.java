package com.tourandtravel.tourandtravelapplication.service;
//src/main/java/com/tourandtravel/service/DestinationService.java

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourandtravel.tourandtravelapplication.model.Destination;
import com.tourandtravel.tourandtravelapplication.repository.DestinationRepository;

@Service
public class DestinationService {
 
 @Autowired
 private DestinationRepository destinationRepository;
 
 public List<Destination> getAllDestinations() {
     return destinationRepository.findAll();
 }
 
 public Optional<Destination> getDestinationById(Long id) {
     return destinationRepository.findById(id);
 }
 
 public List<Destination> getPopularDestinations() {
     return destinationRepository.findByPopularTrue();
 }
 
 public List<Destination> getDestinationsByCountry(String country) {
     return destinationRepository.findByCountry(country);
 }
 
 public Destination createDestination(Destination destination) {
     return destinationRepository.save(destination);
 }
 
 public Destination updateDestination(Destination destination) {
     return destinationRepository.save(destination);
 }
 
 public void deleteDestination(Long id) {
     destinationRepository.deleteById(id);
 }
}