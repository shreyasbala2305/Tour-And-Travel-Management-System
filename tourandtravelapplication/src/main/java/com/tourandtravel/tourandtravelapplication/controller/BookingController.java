package com.tourandtravel.tourandtravelapplication.controller;
//src/main/java/com/tourandtravel/controller/BookingController.java

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tourandtravel.tourandtravelapplication.model.Booking;
import com.tourandtravel.tourandtravelapplication.model.Payment;
import com.tourandtravel.tourandtravelapplication.model.TourPackage;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.service.BookingService;
import com.tourandtravel.tourandtravelapplication.service.PaymentService;
import com.tourandtravel.tourandtravelapplication.service.TourPackageService;
import com.tourandtravel.tourandtravelapplication.service.UserService;

@Controller
@RequestMapping("/booking")
public class BookingController {
 
 @Autowired
 private BookingService bookingService;
 
 @Autowired
 private TourPackageService tourPackageService;
 
 @Autowired
 private UserService userService;
 
 @Autowired
 private PaymentService paymentService;
 
 private User getCurrentUser() {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     return userService.getUserByUsername(authentication.getName())
             .orElseThrow(() -> new RuntimeException("User not found"));
 }
 
 @GetMapping("/new/{packageId}")
 public String newBookingForm(@PathVariable Long packageId, Model model) {
     TourPackage tourPackage = tourPackageService.getTourPackageById(packageId)
             .orElseThrow(() -> new RuntimeException("Tour package not found"));
     
     model.addAttribute("tourPackage", tourPackage);
     model.addAttribute("minDate", LocalDate.now().plusDays(1));
     model.addAttribute("maxDate", LocalDate.now().plusMonths(6));
     
     return "user/new-booking";
 }
 
 @PostMapping("/create")
 public String createBooking(@RequestParam Long packageId,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate,
                            @RequestParam int numberOfPeople,
                            RedirectAttributes redirectAttributes) {
     
     User user = getCurrentUser();
     
     try {
         Booking booking = bookingService.createBooking(user, packageId, travelDate, numberOfPeople);
         redirectAttributes.addFlashAttribute("success", "Booking created successfully");
         return "redirect:/booking/payment/" + booking.getId();
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("error", e.getMessage());
         return "redirect:/booking/new/" + packageId;
     }
 }
 
 @GetMapping("/payment/{bookingId}")
 public String paymentForm(@PathVariable Long bookingId, Model model) {
     User user = getCurrentUser();
     Booking booking = bookingService.getBookingById(bookingId)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     
     // Ensure the booking belongs to the current user
     if (!booking.getUser().getId().equals(user.getId())) {
         throw new RuntimeException("Unauthorized access to booking");
     }
     
     model.addAttribute("booking", booking);
     model.addAttribute("paymentMethods", Payment.PaymentMethod.values());
     
     return "user/payment";
 }
 
 @PostMapping("/payment/process")
 public String processPayment(@RequestParam Long bookingId,
                             @RequestParam Payment.PaymentMethod paymentMethod,
                             @RequestParam String transactionId,
                             RedirectAttributes redirectAttributes) {
     
     User user = getCurrentUser();
     Booking booking = bookingService.getBookingById(bookingId)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     
     // Ensure the booking belongs to the current user
     if (!booking.getUser().getId().equals(user.getId())) {
         throw new RuntimeException("Unauthorized access to booking");
     }
     
     try {
         paymentService.processPayment(bookingId, paymentMethod, transactionId);
         redirectAttributes.addFlashAttribute("success", "Payment processed successfully");
         return "redirect:/user/bookings/" + bookingId;
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("error", e.getMessage());
         return "redirect:/booking/payment/" + bookingId;
     }
 }
 
 @PostMapping("/cancel/{bookingId}")
 public String cancelBooking(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
     User user = getCurrentUser();
     Booking booking = bookingService.getBookingById(bookingId)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     
     // Ensure the booking belongs to the current user
     if (!booking.getUser().getId().equals(user.getId())) {
         throw new RuntimeException("Unauthorized access to booking");
     }
     
     bookingService.updateBookingStatus(bookingId, Booking.BookingStatus.CANCELLED);
     redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
     
     return "redirect:/user/bookings";
 }
}