package com.tourandtravel.tourandtravelapplication.controller;
//src/main/java/com/tourandtravel/controller/AdminController.java

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tourandtravel.tourandtravelapplication.model.Booking;
import com.tourandtravel.tourandtravelapplication.model.Destination;
import com.tourandtravel.tourandtravelapplication.model.TourPackage;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.service.BookingService;
import com.tourandtravel.tourandtravelapplication.service.DestinationService;
import com.tourandtravel.tourandtravelapplication.service.TourPackageService;
import com.tourandtravel.tourandtravelapplication.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
 
 @Autowired
 private UserService userService;
 
 @Autowired
 private TourPackageService tourPackageService;
 
 @Autowired
 private BookingService bookingService;
 
 @Autowired
 private DestinationService destinationService;
 
 @GetMapping("/dashboard")
 public String adminDashboard(Model model) {
     List<User> users = userService.getAllUsers();
     List<TourPackage> tourPackages = tourPackageService.getAllTourPackages();
     List<Booking> bookings = bookingService.getAllBookings();
     
     model.addAttribute("userCount", users.size());
     model.addAttribute("packageCount", tourPackages.size());
     model.addAttribute("bookingCount", bookings.size());
     model.addAttribute("recentBookings", bookings.subList(0, Math.min(5, bookings.size())));
     
     return "admin/dashboard";
 }
 
 // User Management
 @GetMapping("/users")
 public String listUsers(Model model) {
     List<User> users = userService.getAllUsers();
     model.addAttribute("users", users);
     return "admin/users/list";
 }
 
 @GetMapping("/users/{id}")
 public String viewUser(@PathVariable Long id, Model model) {
     User user = userService.getUserById(id)
             .orElseThrow(() -> new RuntimeException("User not found"));
     model.addAttribute("user", user);
     return "admin/users/view";
 }
 
 // Tour Package Management
 @GetMapping("/packages")
 public String listPackages(Model model) {
     List<TourPackage> packages = tourPackageService.getAllTourPackages();
     model.addAttribute("packages", packages);
     return "admin/packages/list";
 }
 
 @GetMapping("/packages/new")
 public String newPackageForm(Model model) {
     model.addAttribute("tourPackage", new TourPackage());
     model.addAttribute("destinations", destinationService.getAllDestinations());
     return "admin/packages/form";
 }
 
 @PostMapping("/packages/save")
 public String savePackage(@ModelAttribute TourPackage tourPackage, RedirectAttributes redirectAttributes) {
     tourPackageService.createTourPackage(tourPackage);
     redirectAttributes.addFlashAttribute("success", "Tour package saved successfully");
     return "redirect:/admin/packages";
 }
 
 @GetMapping("/packages/edit/{id}")
 public String editPackageForm(@PathVariable Long id, Model model) {
     TourPackage tourPackage = tourPackageService.getTourPackageById(id)
             .orElseThrow(() -> new RuntimeException("Tour package not found"));
     model.addAttribute("tourPackage", tourPackage);
     model.addAttribute("destinations", destinationService.getAllDestinations());
     return "admin/packages/form";
 }
 
 @GetMapping("/packages/delete/{id}")
 public String deletePackage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
     tourPackageService.deleteTourPackage(id);
     redirectAttributes.addFlashAttribute("success", "Tour package deleted successfully");
     return "redirect:/admin/packages";
 }
 
 // Booking Management
 @GetMapping("/bookings")
 public String listBookings(Model model) {
     List<Booking> bookings = bookingService.getAllBookings();
     model.addAttribute("bookings", bookings);
     return "admin/bookings/list";
 }
 
 @GetMapping("/bookings/{id}")
 public String viewBooking(@PathVariable Long id, Model model) {
     Booking booking = bookingService.getBookingById(id)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     model.addAttribute("booking", booking);
     return "admin/bookings/view";
 }
 
 @PostMapping("/bookings/{id}/status")
 public String updateBookingStatus(@PathVariable Long id, @RequestParam Booking.BookingStatus status,
                                   RedirectAttributes redirectAttributes) {
     bookingService.updateBookingStatus(id, status);
     redirectAttributes.addFlashAttribute("success", "Booking status updated successfully");
     return "redirect:/admin/bookings/" + id;
 }
 
 // Destination Management
 @GetMapping("/destinations")
 public String listDestinations(Model model) {
     List<Destination> destinations = destinationService.getAllDestinations();
     model.addAttribute("destinations", destinations);
     return "admin/destinations/list";
 }
 
 @GetMapping("/destinations/new")
 public String newDestinationForm(Model model) {
     model.addAttribute("destination", new Destination());
     return "admin/destinations/form";
 }
 
 @PostMapping("/destinations/save")
 public String saveDestination(@ModelAttribute Destination destination, RedirectAttributes redirectAttributes) {
     destinationService.createDestination(destination);
     redirectAttributes.addFlashAttribute("success", "Destination saved successfully");
     return "redirect:/admin/destinations";
 }
 
 @GetMapping("/destinations/edit/{id}")
 public String editDestinationForm(@PathVariable Long id, Model model) {
     Destination destination = destinationService.getDestinationById(id)
             .orElseThrow(() -> new RuntimeException("Destination not found"));
     model.addAttribute("destination", destination);
     return "admin/destinations/form";
 }
 
 @GetMapping("/destinations/delete/{id}")
 public String deleteDestination(@PathVariable Long id, RedirectAttributes redirectAttributes) {
     destinationService.deleteDestination(id);
     redirectAttributes.addFlashAttribute("success", "Destination deleted successfully");
     return "redirect:/admin/destinations";
 }
}