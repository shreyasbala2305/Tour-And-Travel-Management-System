package com.tourandtravel.tourandtravelapplication.service;
//src/main/java/com/tourandtravel/service/NotificationService.java

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourandtravel.tourandtravelapplication.model.Notification;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.repository.NotificationRepository;

@Service
public class NotificationService {
 
 @Autowired
 private NotificationRepository notificationRepository;
 
 public List<Notification> getNotificationsByUser(User user) {
     return notificationRepository.findByUserOrderByCreatedAtDesc(user);
 }
 
 public List<Notification> getUnreadNotificationsByUser(User user) {
     return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
 }
 
 public Notification createNotification(User user, String message, LocalDateTime createdAt, Notification.NotificationType type) {
     Notification notification = new Notification();
     notification.setUser(user);
     notification.setMessage(message);
     notification.setCreatedAt(createdAt);
     notification.setRead(false);
     notification.setType(type);
     
     return notificationRepository.save(notification);
 }
 
 public Notification markAsRead(Long notificationId) {
     Notification notification = notificationRepository.findById(notificationId)
             .orElseThrow(() -> new RuntimeException("Notification not found"));
     
     notification.setRead(true);
     return notificationRepository.save(notification);
 }
 
 public void markAllAsRead(User user) {
     List<Notification> notifications = notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
     for (Notification notification : notifications) {
         notification.setRead(true);
         notificationRepository.save(notification);
     }
 }
}