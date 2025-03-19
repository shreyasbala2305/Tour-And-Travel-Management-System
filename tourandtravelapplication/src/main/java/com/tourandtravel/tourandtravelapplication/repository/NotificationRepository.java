package com.tourandtravel.tourandtravelapplication.repository;
//src/main/java/com/tourandtravel/repository/NotificationRepository.java

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourandtravel.tourandtravelapplication.model.Notification;
import com.tourandtravel.tourandtravelapplication.model.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
 List<Notification> findByUserOrderByCreatedAtDesc(User user);
 List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(User user);
}