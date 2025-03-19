package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/User.java

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

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
 
 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
 private Set<Booking> bookings = new HashSet<>();
 
 public enum UserRole {
     ROLE_USER, ROLE_ADMIN
 }
}