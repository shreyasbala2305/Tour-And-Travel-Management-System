package com.tourandtravel.tourandtravelapplication.config;
//src/main/java/com/tourandtravel/config/SecurityConfig.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

 @Autowired
 private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
         .authorizeHttpRequests(authz -> authz
             .requestMatchers("/", "/home", "/register", "/login", "/static/**", "/destination/**").permitAll()
             .requestMatchers("/admin/**").hasRole("ADMIN")
             .requestMatchers("/user/**").hasRole("USER")
             .anyRequest().authenticated()
         )
         .formLogin(form -> form
             .loginPage("/login")
             .defaultSuccessUrl("/dashboard")
             .permitAll()
         )
         .logout(logout -> logout
             .logoutSuccessUrl("/login?logout")
             .permitAll()
         );
     return http.build();
 }

 @Autowired
 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
     auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
 }

    @Bean
    PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }
}