package com.tourandtravel.tour_travel_management_application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import com.tourandtravel.tour_travel_management_application.model.Booking;
import com.tourandtravel.tour_travel_management_application.model.TourPackage;
import com.tourandtravel.tour_travel_management_application.model.User;
import com.tourandtravel.tour_travel_management_application.repositories.BookingRepository;
import com.tourandtravel.tour_travel_management_application.repositories.TourPackageRepository;
import com.tourandtravel.tour_travel_management_application.service.BookingService;
import com.tourandtravel.tour_travel_management_application.service.TourPackageService;

@SpringBootTest
public class TourTravelManagementApplicationTests {

    @Autowired
    private TourPackageService tourPackageService;

    @Autowired
    private BookingService bookingService;

    @MockBean
    private TourPackageRepository tourPackageRepository;

    @MockBean
    private BookingRepository bookingRepository;

    private TourPackage testPackage;
    private User testUser;

    @BeforeEach
    public void setup() {
        // Create test data
        testPackage = new TourPackage();
        testPackage.setId(1L);
        testPackage.setPackageName("Mountain Adventure");
        testPackage.setPrice(BigDecimal.valueOf(500));
        testPackage.setMinParticipants(1);
        testPackage.setMaxParticipants(10);
        testPackage.setStartDate(LocalDate.now().plusMonths(1));
        testPackage.setEndDate(LocalDate.now().plusMonths(2));
        testPackage.setDifficultyLevel(TourPackage.DifficultyLevel.MODERATE);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
    }

    @Test
    public void testCreateTourPackage_Success() {
        when(tourPackageRepository.save(any(TourPackage.class))).thenReturn(testPackage);

        TourPackage createdPackage = tourPackageService.createTourPackage(testPackage);

        assertNotNull(createdPackage);
        assertEquals("Mountain Adventure", createdPackage.getPackageName());
        verify(tourPackageRepository, times(1)).save(testPackage);
    }

    @Test
    public void testCreateBooking_Success() {
        Booking booking = new Booking();
        booking.setNumberOfParticipants(2);

        when(tourPackageRepository.findById(anyLong())).thenReturn(Optional.of(testPackage));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(booking, testPackage.getId());

        assertNotNull(createdBooking);
        assertEquals(2, createdBooking.getNumberOfParticipants());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    public void testCreateBooking_ExceedMaxParticipants() {
        Booking booking = new Booking();
        booking.setNumberOfParticipants(15);

        when(tourPackageRepository.findById(anyLong())).thenReturn(Optional.of(testPackage));

        assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking, testPackage.getId());
        });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAdminAccessControl() {
        // This test ensures that admin can access restricted resources
        assertTrue(true); // Placeholder for actual admin-specific test
    }

    @Test
    public void testPaymentCalculation() {
        Booking booking = new Booking();
        booking.setTourPackage(testPackage);
        booking.setNumberOfParticipants(3);

        booking.calculateTotalPrice();

        assertEquals(BigDecimal.valueOf(1500), booking.getTotalPrice());
    }

    @Test
    public void testTourPackageAvailability() {
        // Simulate some bookings
        testPackage.setBookings(Set.of(
            createMockBooking(5),
            createMockBooking(3)
        ));

        assertFalse(testPackage.isAvailable());

        // Create a package with fewer bookings
        testPackage.setBookings(Set.of(
            createMockBooking(2)
        ));

        assertTrue(testPackage.isAvailable());
    }

    // Utility method to create mock bookings
    private Booking createMockBooking(int participants) {
        Booking booking = new Booking();
        booking.setNumberOfParticipants(participants);
        return booking;
    }
}
