package com.tourandtravel.tourandtravelapplication.service;
//src/main/java/com/tourandtravel/service/BookingService.java

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourandtravel.tourandtravelapplication.model.Booking;
import com.tourandtravel.tourandtravelapplication.model.Payment;
import com.tourandtravel.tourandtravelapplication.model.TourPackage;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.repository.BookingRepository;

@Service
public class BookingService {
 
 @Autowired
 private BookingRepository bookingRepository;
 
 @Autowired
 private TourPackageService tourPackageService;
 
 @Autowired
 private PaymentService paymentService;
 
 @Autowired
 private NotificationService notificationService;
 
 public List<Booking> getAllBookings() {
     return bookingRepository.findAll();
 }
 
 public Optional<Booking> getBookingById(Long id) {
     return bookingRepository.findById(id);
 }
 
 public List<Booking> getBookingsByUserId(Long userId) {
     return bookingRepository.findByUserId(userId);
 }
 
 public Booking createBooking(User user, Long tourPackageId, LocalDate travelDate, int numberOfPeople) {
     TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId)
             .orElseThrow(() -> new RuntimeException("Tour package not found"));
     
     BigDecimal totalAmount = tourPackage.getPrice().multiply(BigDecimal.valueOf(numberOfPeople));
     
     Booking booking = new Booking();
     booking.setUser(user);
     booking.setTourPackage(tourPackage);
     booking.setBookingDate(LocalDate.now());
     booking.setTravelDate(travelDate);
     booking.setNumberOfPeople(numberOfPeople);
     booking.setTotalAmount(totalAmount);
     booking.setStatus(Booking.BookingStatus.PENDING);
     
     Booking savedBooking = bookingRepository.save(booking);
     
     // Create a pending payment
     Payment payment = new Payment();
     payment.setBooking(savedBooking);
     payment.setAmount(totalAmount);
     payment.setStatus(Payment.PaymentStatus.PENDING);
     paymentService.savePayment(payment);
     
     // Send notification to user
     notificationService.createNotification(
             user,
             "Your booking for " + tourPackage.getName() + " is pending. Please complete the payment.",
             LocalDateTime.now(),
             com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.BOOKING_CONFIRMATION
     );
     
     return savedBooking;
 }
 
 public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
     Booking booking = bookingRepository.findById(bookingId)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     
     booking.setStatus(status);
     
     // Send notification based on the new status
     String message = "";
     com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType type = com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.SYSTEM;
     
     switch (status) {
         case CONFIRMED:
             message = "Your booking for " + booking.getTourPackage().getName() + " has been confirmed.";
             type = com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.BOOKING_CONFIRMATION;
             break;
         case CANCELLED:
             message = "Your booking for " + booking.getTourPackage().getName() + " has been cancelled.";
             type = com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.TOUR_UPDATE;
             break;
         case COMPLETED:
             message = "Your tour for " + booking.getTourPackage().getName() + " has been completed. Thank you for traveling with us!";
             type = com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.TOUR_UPDATE;
             break;
		case PENDING:
			break;
		default:
			break;
     }
     
     notificationService.createNotification(
             booking.getUser(),
             message,
             LocalDateTime.now(),
             type
     );
     
     return bookingRepository.save(booking);
 }
 
 public void deleteBooking(Long id) {
     bookingRepository.deleteById(id);
 }
}