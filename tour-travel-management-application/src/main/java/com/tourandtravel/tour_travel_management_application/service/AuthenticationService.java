package com.tourandtravel.tour_travel_management_application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourandtravel.tour_travel_management_application.model.Role;
import com.tourandtravel.tour_travel_management_application.model.User;
import com.tourandtravel.tour_travel_management_application.repositories.RoleRepository;
import com.tourandtravel.tour_travel_management_application.repositories.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public User registerNewUser(User user) {
        // Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if no roles are assigned
        if (user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            user.addRole(userRole);
        }

        // Save user
        return userRepository.save(user);
    }

    public String authenticateUser(String username, String password) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Generate JWT token
        return jwtTokenService.generateToken(authentication);
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();

        // In a real-world scenario, you would store this token in the database
        // and associate it with the user for verification

        // Send password reset email
        sendPasswordResetEmail(user, resetToken);
    }

    private void sendPasswordResetEmail(User user, String resetToken) {
        String resetLink = "https://yourdomain.com/reset-password?token=" + resetToken;

        // Use notification service to send email
        // This is a simplified version - in production, you'd create a more secure process
        notificationService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }
}