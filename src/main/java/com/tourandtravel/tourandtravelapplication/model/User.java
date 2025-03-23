package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/User.java

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @Column(nullable = false, unique = true)
 private String username;
 
 @Column(nullable = false)
 private String password;
 
 @Column(nullable = false)
 private String fullName;
 
 @Column(nullable = false, unique = true)
 private String email;
 
 private String phoneNumber;
 
 private String address;
 
 @Enumerated(EnumType.STRING)
 private UserRole role;
 
 private boolean enabled = true;
 
 private LocalDate creationDate;
 
 private LocalDate lastLogin;
 
 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
 private Set<Booking> bookings = new HashSet<>();
 
 public enum UserRole {
     ROLE_USER, ROLE_ADMIN
 }
}