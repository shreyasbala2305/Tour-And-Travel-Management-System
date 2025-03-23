package com.tourandtravel.tourandtravelapplication.controller;
//src/main/java/com/tourandtravel/controller/UserController.java

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tourandtravel.tourandtravelapplication.model.Booking;
import com.tourandtravel.tourandtravelapplication.model.Notification;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.service.BookingService;
import com.tourandtravel.tourandtravelapplication.service.NotificationService;
import com.tourandtravel.tourandtravelapplication.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
 
 @Autowired
 private UserService userService;
 
 @Autowired
 private BookingService bookingService;
 
 @Autowired
 private NotificationService notificationService;
 
 private User getCurrentUser() {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     return userService.getUserByUsername(authentication.getName())
             .orElseThrow(() -> new RuntimeException("User not found"));
 }
 
 @GetMapping("/dashboard")
 @PreAuthorize("hasRole('ROLE_USER')")
 public String userDashboard(Model model) {
     User user = getCurrentUser();
     List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());
     List<Notification> notifications = notificationService.getUnreadNotificationsByUser(user);
     
     long upcomingBookingsCount = bookings.stream()
    	        .filter(b -> b.getTravelDate().isAfter(LocalDate.now()))
    	        .count();
     
     model.addAttribute("user", user);
     model.addAttribute("bookings", bookings);
     model.addAttribute("notifications", notifications);
     
     return "user/dashboard";
 }
 
 @GetMapping("/profile")
 public String viewProfile(Model model) {
     User user = getCurrentUser();
     model.addAttribute("user", user);
     return "user/profile";
 }
 
 @PostMapping("/profile/update")
 public String updateProfile(@ModelAttribute User updatedUser, RedirectAttributes redirectAttributes) {
     User user = getCurrentUser();
     
     // Update only allowed fields
     user.setFullName(updatedUser.getFullName());
     user.setPhoneNumber(updatedUser.getPhoneNumber());
     user.setAddress(updatedUser.getAddress());
     
     userService.updateUser(user);
     redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
     return "redirect:/user/profile";
 }
 
 @GetMapping("/bookings")
 public String viewBookings(Model model) {
     User user = getCurrentUser();
     List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());
     model.addAttribute("bookings", bookings);
     return "user/bookings";
 }
 
 @GetMapping("/bookings/{id}")
 public String viewBookingDetails(@PathVariable("id") Long id, Model model) {
     User user = getCurrentUser();
     Booking booking = bookingService.getBookingById(id)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     
     // Ensure the booking belongs to the current user
     if (!booking.getUser().getId().equals(user.getId())) {
         throw new RuntimeException("Unauthorized access to booking");
     }
     
     model.addAttribute("booking", booking);
     return "user/booking-details";
 }
 
 @GetMapping("/notifications")
 public String viewNotifications(Model model) {
     User user = getCurrentUser();
     List<Notification> notifications = notificationService.getNotificationsByUser(user);
     model.addAttribute("notifications", notifications);
     return "user/notifications";
 }
 
 @PostMapping("/notifications/read/{id}")
 public String markNotificationAsRead(@PathVariable Long id) {
     notificationService.markAsRead(id);
     return "redirect:/user/notifications";
 }
 
 @PostMapping("/notifications/read-all")
 public String markAllNotificationsAsRead() {
     User user = getCurrentUser();
     notificationService.markAllAsRead(user);
     return "redirect:/user/notifications";
 }
}