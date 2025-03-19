package com.tourandtravel.tourandtravelapplication.controller;
//src/main/java/com/tourandtravel/controller/AuthController.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.service.UserService;

@Controller
public class AuthController {
 
 @Autowired
 private UserService userService;
 
 @GetMapping("/login")
 public String loginPage() {
     return "common/login";
 }
 
 @GetMapping("/register")
 public String registerPage(Model model) {
     model.addAttribute("user", new User());
     return "common/register";
 }
 
 @PostMapping("/register")
 public String registerUser(@ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
     if (result.hasErrors()) {
         return "common/register";
     }
     
     try {
         userService.registerNewUser(user);
         redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
         return "redirect:/login";
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("error", e.getMessage());
         return "redirect:/register";
     }
 }
}