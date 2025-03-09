package com.tourandtravel.tour_travel_management_application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourandtravel.tour_travel_management_application.model.Booking;
import com.tourandtravel.tour_travel_management_application.model.Payment;
import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.repositories.BookingRepository;
import com.tourandtravel.tour_travel_management_application.repositories.PaymentRepository;
import com.tourandtravel.tour_travel_management_application.repositories.TourPackageRepository;

@Service
public class ReportService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    public Map<String, Object> generateBookingReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();

        // Bookings in date range
        List<Booking> bookings = bookingRepository.findBookingsByDateRange(startDate, endDate);

        // Total bookings
        report.put("totalBookings", bookings.size());

        // Bookings by status
        Map<Booking.BookingStatus, Long> bookingsByStatus = bookings.stream()
            .collect(Collectors.groupingBy(Booking::getStatus, Collectors.counting()));
        report.put("bookingsByStatus", bookingsByStatus);

        // Revenue calculations
        BigDecimal totalRevenue = bookings.stream()
            .map(Booking::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("totalRevenue", totalRevenue);

        // Most popular tour packages
        Map<TourPackage, Long> popularPackages = bookings.stream()
            .collect(Collectors.groupingBy(Booking::getTourPackage, Collectors.counting()));
        report.put("popularPackages", popularPackages);

        return report;
    }

    public Map<String, Object> generatePaymentReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();

        // Payments in date range
        List<Payment> payments = paymentRepository.findAll().stream()
            .filter(p -> p.getPaymentDate().isAfter(startDate) && p.getPaymentDate().isBefore(endDate))
            .collect(Collectors.toList());

        // Total payments
        report.put("totalPayments", payments.size());

        // Payments by status
        Map<Payment.PaymentStatus, Long> paymentsByStatus = payments.stream()
            .collect(Collectors.groupingBy(Payment::getStatus, Collectors.counting()));
        report.put("paymentsByStatus", paymentsByStatus);

        // Total revenue
        BigDecimal totalRevenue = payments.stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("totalRevenue", totalRevenue);

        // Payment methods distribution
        Map<Payment.PaymentMethod, Long> paymentMethodDistribution = payments.stream()
            .collect(Collectors.groupingBy(Payment::getPaymentMethod, Collectors.counting()));
        report.put("paymentMethodDistribution", paymentMethodDistribution);

        return report;
    }

    public Map<String, Object> generateTourPackageReport() {
        Map<String, Object> report = new HashMap<>();

        // All tour packages
        List<TourPackage> tourPackages = tourPackageRepository.findAll();

        // Total packages
        report.put("totalPackages", tourPackages.size());

        // Packages by difficulty level
        Map<TourPackage.DifficultyLevel, Long> packagesByDifficulty = tourPackages.stream()
            .collect(Collectors.groupingBy(TourPackage::getDifficultyLevel, Collectors.counting()));
        report.put("packagesByDifficulty", packagesByDifficulty);

        // Average package price
        BigDecimal averagePackagePrice = tourPackages.stream()
            .map(TourPackage::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(tourPackages.size()), 2, BigDecimal.ROUND_HALF_UP);
        report.put("averagePackagePrice", averagePackagePrice);

        // Upcoming packages
        List<TourPackage> upcomingPackages = tourPackages.stream()
            .filter(p -> p.getStartDate().isAfter(LocalDateTime.now().toLocalDate()))
            .collect(Collectors.toList());
        report.put("upcomingPackages", upcomingPackages);

        return report;
    }
}
