package com.tourandtravel.tourandtravelapplication.controller;
//src/main/java/com/tourandtravel/controller/HomeController.java

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.tourandtravel.tourandtravelapplication.service.DestinationService;
import com.tourandtravel.tourandtravelapplication.model.Destination;
import com.tourandtravel.tourandtravelapplication.model.TourPackage;
import com.tourandtravel.tourandtravelapplication.service.TourPackageService;

@Controller
public class HomeController {
 
 @Autowired
 private TourPackageService tourPackageService;
 
 @Autowired
 private DestinationService destinationService;
 
 @GetMapping("/")
 public String home(Model model) {
     List<TourPackage> featuredPackages = tourPackageService.getFeaturedTourPackages();
     List<Destination> popularDestinations = destinationService.getPopularDestinations();
     
     model.addAttribute("featuredPackages", featuredPackages);
     model.addAttribute("popularDestinations", popularDestinations);
     
     return "common/home";
 }
 
 @GetMapping("/packages")
 public String allPackages(Model model) {
     List<TourPackage> packages = tourPackageService.getAllTourPackages();
     model.addAttribute("packages", packages);
     return "common/packages";
 }
 
 @GetMapping("/packages/{id}")
 public String packageDetails(@PathVariable Long id, Model model) {
     TourPackage tourPackage = tourPackageService.getTourPackageById(id)
             .orElseThrow(() -> new RuntimeException("Tour package not found"));
     model.addAttribute("package", tourPackage);
     return "common/package-details";
 }
 
 @GetMapping("/destinations")
 public String allDestinations(Model model) {
     List<Destination> destinations = destinationService.getAllDestinations();
     model.addAttribute("destinations", destinations);
     return "common/destinations";
 }
 
 @GetMapping("/destinations/{id}")
 public String destinationDetails(@PathVariable Long id, Model model) {
     Destination destination = destinationService.getDestinationById(id)
             .orElseThrow(() -> new RuntimeException("Destination not found"));
     List<TourPackage> packages = tourPackageService.getTourPackagesByDestination(id);
     
     model.addAttribute("destination", destination);
     model.addAttribute("packages", packages);
     
     return "common/destination-details";
 }
 
 @GetMapping("/search")
 public String searchPackages(@RequestParam String query, Model model) {
     List<TourPackage> searchResults = tourPackageService.searchTourPackages(query);
     model.addAttribute("searchResults", searchResults);
     model.addAttribute("query", query);
     return "common/search-results";
 }
 
 @GetMapping("/about")
 public String about() {
     return "common/about";
 }
 
 @GetMapping("/contact")
 public String contact() {
     return "common/contact";
 }
}