package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/Notification.java

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @ManyToOne
 @JoinColumn(name = "user_id")
 private User user;
 
 private String message;
 
 private LocalDateTime createdAt;
 
 private boolean isread = false;
 
 @Enumerated(EnumType.STRING)
 private NotificationType type;
 
 public enum NotificationType {
     BOOKING_CONFIRMATION, PAYMENT_CONFIRMATION, TOUR_UPDATE, SYSTEM
 }
}