package com.tourandtravel.tour_travel_management_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tourandtravel.tour_travel_management_application.model.Booking;
import com.tourandtravel.tour_travel_management_application.model.User;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendBookingConfirmationEmail(Booking booking) {
        User user = booking.getUser();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Booking Confirmation - " + booking.getTourPackage().getPackageName());
        message.setText(createBookingConfirmationMessage(booking));

        mailSender.send(message);
    }

    @Async
    public void sendPaymentReminderEmail(Booking booking) {
        User user = booking.getUser();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Payment Reminder - " + booking.getTourPackage().getPackageName());
        message.setText(createPaymentReminderMessage(booking));

        mailSender.send(message);
    }
    
    @Async
    public void sendBookingCancellationEmail(Booking booking) {
        User user = booking.getUser();
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Booking Cancellation - " + booking.getTourPackage().getPackageName());
        message.setText(createBookingCancellationMessage(booking));
        
        mailSender.send(message);
    }

    private String createBookingConfirmationMessage(Booking booking) {
        return String.format(
            "Dear %s,\n\n" +
            "Your booking for %s has been confirmed!\n\n" +
            "Booking Details:\n" +
            "Package: %s\n" +
            "Number of Participants: %d\n" +
            "Total Price: $%.2f\n" +
            "Booking Date: %s\n\n" +
            "Thank you for choosing our travel service!\n\n" +
            "Best regards,\n" +
            "Tour and Travel Management Team",
            booking.getUser().getFullName(),
            booking.getTourPackage().getPackageName(),
            booking.getTourPackage().getPackageName(),
            booking.getNumberOfParticipants(),
            booking.getTotalPrice(),
            booking.getBookingDate()
        );
    }

    private String createPaymentReminderMessage(Booking booking) {
        return String.format(
            "Dear %s,\n\n" +
            "This is a friendly reminder about your upcoming tour package payment.\n\n" +
            "Package: %s\n" +
            "Total Amount Due: $%.2f\n\n" +
            "Please complete your payment to confirm your booking.\n\n" +
            "Best regards,\n" +
            "Tour and Travel Management Team",
            booking.getUser().getFullName(),
            booking.getTourPackage().getPackageName(),
            booking.getTotalPrice()
        );
    }
    
    private String createBookingCancellationMessage(Booking booking) {
        return String.format(
            "Dear %s,\n\n" +
            "Your booking for %s has been cancelled.\n\n" +
            "Cancellation Details:\n" +
            "Package: %s\n" +
            "Number of Participants: %d\n" +
            "Total Amount: $%.2f\n" +
            "Booking Date: %s\n\n" +
            "If you have any questions about this cancellation or would like to book another package, " +
            "please don't hesitate to contact our customer service team.\n\n" +
            "Best regards,\n" +
            "Tour and Travel Management Team",
            booking.getUser().getFullName(),
            booking.getTourPackage().getPackageName(),
            booking.getTourPackage().getPackageName(),
            booking.getNumberOfParticipants(),
            booking.getTotalPrice(),
            booking.getBookingDate()
        );
    }

	public void sendPasswordResetEmail(String email, String resetLink) {
		// TODO Auto-generated method stub
		
	}
}