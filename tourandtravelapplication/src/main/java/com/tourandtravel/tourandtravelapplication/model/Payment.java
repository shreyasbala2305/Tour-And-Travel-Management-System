package com.tourandtravel.tourandtravelapplication.model;
//src/main/java/com/tourandtravel/model/Payment.java

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @OneToOne
 @JoinColumn(name = "booking_id")
 private Booking booking;
 
 private BigDecimal amount;
 
 private LocalDateTime paymentDate;
 
 private String transactionId;
 
 @Enumerated(EnumType.STRING)
 private PaymentMethod paymentMethod;
 
 @Enumerated(EnumType.STRING)
 private PaymentStatus status;
 
 public enum PaymentMethod {
     CREDIT_CARD, PAYPAL, BANK_TRANSFER
 }
 
 public enum PaymentStatus {
     PENDING, COMPLETED, FAILED, REFUNDED
 }
}