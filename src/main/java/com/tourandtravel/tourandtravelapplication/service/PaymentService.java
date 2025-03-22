package com.tourandtravel.tourandtravelapplication.service;
//src/main/java/com/tourandtravel/service/PaymentService.java

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourandtravel.tourandtravelapplication.model.Booking;
import com.tourandtravel.tourandtravelapplication.model.Payment;
import com.tourandtravel.tourandtravelapplication.repository.PaymentRepository;

@Service
public class PaymentService {
 
 @Autowired
 private PaymentRepository paymentRepository;
 
 @Autowired
 private BookingService bookingService;
 
 @Autowired
 private NotificationService notificationService;
 
 public List<Payment> getAllPayments() {
     return paymentRepository.findAll();
 }
 
 public Optional<Payment> getPaymentById(Long id) {
     return paymentRepository.findById(id);
 }
 
 public Optional<Payment> getPaymentByBookingId(Long bookingId) {
     return paymentRepository.findByBookingId(bookingId);
 }
 
 public Payment savePayment(Payment payment) {
     return paymentRepository.save(payment);
 }
 
 public Payment processPayment(Long bookingId, Payment.PaymentMethod paymentMethod, String transactionId) {
     Booking booking = bookingService.getBookingById(bookingId)
             .orElseThrow(() -> new RuntimeException("Booking not found"));
     
     Payment payment = paymentRepository.findByBookingId(bookingId)
             .orElse(new Payment());
     
     payment.setBooking(booking);
     payment.setAmount(booking.getTotalAmount());
     payment.setPaymentMethod(paymentMethod);
     payment.setTransactionId(transactionId);
     payment.setPaymentDate(LocalDateTime.now());
     payment.setStatus(Payment.PaymentStatus.COMPLETED);
     
     Payment savedPayment = paymentRepository.save(payment);
     
     // Update booking status
     bookingService.updateBookingStatus(bookingId, Booking.BookingStatus.CONFIRMED);
     
     // Send notification
     notificationService.createNotification(
             booking.getUser(),
             "Payment confirmed for your booking of " + booking.getTourPackage().getName() + ".",
             LocalDateTime.now(),
             com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.PAYMENT_CONFIRMATION
     );
     
     return savedPayment;
 }
 
 public Payment refundPayment(Long paymentId) {
     Payment payment = paymentRepository.findById(paymentId)
             .orElseThrow(() -> new RuntimeException("Payment not found"));
     
     payment.setStatus(Payment.PaymentStatus.REFUNDED);
     Payment savedPayment = paymentRepository.save(payment);
     
     // Update booking status
     bookingService.updateBookingStatus(payment.getBooking().getId(), Booking.BookingStatus.CANCELLED);
     
     // Send notification
     notificationService.createNotification(
             payment.getBooking().getUser(),
             "Refund processed for your booking of " + payment.getBooking().getTourPackage().getName() + ".",
             LocalDateTime.now(),
             com.tourandtravel.tourandtravelapplication.model.Notification.NotificationType.PAYMENT_CONFIRMATION
     );
     
     return savedPayment;
 }
}