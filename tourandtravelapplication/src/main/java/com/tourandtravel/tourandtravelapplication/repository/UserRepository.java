package com.tourandtravel.tourandtravelapplication.repository;
//src/main/java/com/tourandtravel/repository/UserRepository.java

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourandtravel.tourandtravelapplication.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
 Optional<User> findByUsername(String username);
 Optional<User> findByEmail(String email);
 boolean existsByUsername(String username);
 boolean existsByEmail(String email);
}

