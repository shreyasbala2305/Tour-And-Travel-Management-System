package com.tourandtravel.tourandtravelapplication.config;
//src/main/java/com/tourandtravel/config/DataInitializer.java

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tourandtravel.tourandtravelapplication.model.Destination;
import com.tourandtravel.tourandtravelapplication.model.TourPackage;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.repository.DestinationRepository;
import com.tourandtravel.tourandtravelapplication.repository.TourPackageRepository;
import com.tourandtravel.tourandtravelapplication.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DestinationRepository destinationRepository;
    
    @Autowired
    private TourPackageRepository tourPackageRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${admin.username}")
    private String adminUsername;
    
    @Value("${admin.password}")
    private String adminPassword;
    
    @Value("${admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setFullName("System Administrator");
            admin.setRole(User.UserRole.ROLE_ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("Admin user created");
        }
        
        // Add sample destinations if none exist
        if (destinationRepository.count() == 0) {
            createSampleDestinations();
            System.out.println("Sample destinations created");
        }
        
        // Add sample tour packages if none exist
        if (tourPackageRepository.count() == 0) {
            createSampleTourPackages();
            System.out.println("Sample tour packages created");
        }
    }
    
    private void createSampleDestinations() {
        // Create sample destinations
        Destination paris = new Destination();
        paris.setName("Paris");
        paris.setDescription("The City of Light with the iconic Eiffel Tower and world-class museums.");
        paris.setCountry("France");
        paris.setImageUrl("paris.jpg");
        paris.setPopular(true);
        destinationRepository.save(paris);
        
        Destination tokyo = new Destination();
        tokyo.setName("Tokyo");
        tokyo.setDescription("A bustling metropolis that perfectly blends ultramodern and traditional.");
        tokyo.setCountry("Japan");
        tokyo.setImageUrl("tokyo.jpg");
        tokyo.setPopular(true);
        destinationRepository.save(tokyo);
        
        Destination bali = new Destination();
        bali.setName("Bali");
        bali.setDescription("A tropical paradise with beautiful beaches, stunning temples, and rich culture.");
        bali.setCountry("Indonesia");
        bali.setImageUrl("bali.jpg");
        bali.setPopular(true);
        destinationRepository.save(bali);
    }
    
    private void createSampleTourPackages() {
        // Get destinations
        Destination paris = destinationRepository.findByName("Paris").orElse(null);
        Destination tokyo = destinationRepository.findByName("Tokyo").orElse(null);
        Destination bali = destinationRepository.findByName("Bali").orElse(null);
        
        if (paris != null) {
            TourPackage parisTour = new TourPackage();
            parisTour.setName("Paris Explorer");
            parisTour.setDescription("Explore the romantic city of Paris with guided tours of iconic landmarks.");
            parisTour.setPrice(new BigDecimal("1299.99"));
            parisTour.setDuration(7);
            parisTour.setImageUrl("paris-tour.jpg");
            parisTour.setMaxGroupSize(15);
            parisTour.setFeatured(true);
            parisTour.setDestination(paris);
            tourPackageRepository.save(parisTour);
        }
        
        if (tokyo != null) {
            TourPackage tokyoTour = new TourPackage();
            tokyoTour.setName("Tokyo Adventure");
            tokyoTour.setDescription("Experience the perfect blend of ancient traditions and cutting-edge technology.");
            tokyoTour.setPrice(new BigDecimal("1899.99"));
            tokyoTour.setDuration(10);
            tokyoTour.setImageUrl("tokyo-tour.jpg");
            tokyoTour.setMaxGroupSize(12);
            tokyoTour.setFeatured(true);
            tokyoTour.setDestination(tokyo);
            tourPackageRepository.save(tokyoTour);
        }
        
        if (bali != null) {
            TourPackage baliTour = new TourPackage();
            baliTour.setName("Bali Relaxation");
            baliTour.setDescription("Unwind in tropical paradise with pristine beaches and luxurious resorts.");
            baliTour.setPrice(new BigDecimal("1499.99"));
            baliTour.setDuration(8);
            baliTour.setImageUrl("bali-tour.jpg");
            baliTour.setMaxGroupSize(10);
            baliTour.setFeatured(true);
            baliTour.setDestination(bali);
            tourPackageRepository.save(baliTour);
        }
    }
}