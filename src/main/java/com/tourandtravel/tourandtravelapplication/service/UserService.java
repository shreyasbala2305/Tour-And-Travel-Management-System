package com.tourandtravel.tourandtravelapplication.service;
//src/main/java/com/tourandtravel/service/UserService.java

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
 
 @Autowired
 private UserRepository userRepository;
 
 @Autowired
 private PasswordEncoder passwordEncoder;
 
 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     User user = userRepository.findByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
     
     GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
     
     return new org.springframework.security.core.userdetails.User(
             user.getUsername(),
             user.getPassword(),
             user.isEnabled(),
             true, true, true,
             Collections.singleton(authority)
     );
 }
 
 public User registerNewUser(User user) {
     if (userRepository.existsByUsername(user.getUsername())) {
         throw new RuntimeException("Username already exists");
     }
     if (userRepository.existsByEmail(user.getEmail())) {
         throw new RuntimeException("Email already in use");
     }
     
     user.setPassword(passwordEncoder.encode(user.getPassword()));
     user.setRole(User.UserRole.ROLE_USER);
     
     return userRepository.save(user);
 }
 
 public List<User> getAllUsers() {
     return userRepository.findAll();
 }
 
 public Optional<User> getUserById(Long id) {
     return userRepository.findById(id);
 }
 
 public Optional<User> getUserByUsername(String username) {
     return userRepository.findByUsername(username);
 }
 
 public User updateUser(User user) {
     return userRepository.save(user);
 }
 
 public void deleteUser(Long id) {
     userRepository.deleteById(id);
 }
}