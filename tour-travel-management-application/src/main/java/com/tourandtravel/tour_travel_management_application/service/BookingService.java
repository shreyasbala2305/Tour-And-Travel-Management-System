package com.tourandtravel.tour_travel_management_application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourandtravel.tour_travel_management_application.model.Booking;
import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.model.User;
import com.tourandtravel.tour_travel_management_application.repositories.BookingRepository;
import com.tourandtravel.tour_travel_management_application.repositories.TourPackageRepository;
import com.tourandtravel.tour_travel_management_application.repositories.UserRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Booking createBooking(Booking booking, Long tourPackageId) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch tour package
        TourPackage tourPackage = tourPackageRepository.findById(tourPackageId)
            .orElseThrow(() -> new RuntimeException("Tour Package not found"));

        // Validate booking
        validateBooking(booking, tourPackage);

        // Set booking details
        booking.setUser(currentUser);
        booking.setTourPackage(tourPackage);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setBookingDate(LocalDateTime.now());
        booking.calculateTotalPrice();

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Send confirmation notification
        notificationService.sendBookingConfirmationEmail(savedBooking);

        return savedBooking;
    }

    public List<Booking> getUserBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUser(currentUser);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Transactional
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking existingBooking = getBookingById(id);

        // Update allowed fields
        existingBooking.setNumberOfParticipants(bookingDetails.getNumberOfParticipants());
        existingBooking.calculateTotalPrice();

        return bookingRepository.save(existingBooking);
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        
        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Optionally, send cancellation notification
        notificationService.sendBookingCancellationEmail(booking);
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    private void validateBooking(Booking booking, TourPackage tourPackage) {
        // Check package availability
        if (!tourPackage.isAvailable()) {
            throw new RuntimeException("Tour package is fully booked");
        }

        // Validate number of participants
        if (booking.getNumberOfParticipants() < tourPackage.getMinParticipants() ||
            booking.getNumberOfParticipants() > tourPackage.getMaxParticipants()) {
            throw new RuntimeException("Number of participants is outside allowed range");
        }

        // Check booking date
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(tourPackage.getStartDate().atStartOfDay())) {
            throw new RuntimeException("Cannot book a tour that has already started");
        }
    }
}
