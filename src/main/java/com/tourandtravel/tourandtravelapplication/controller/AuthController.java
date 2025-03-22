package com.tourandtravel.tourandtravelapplication.controller;
//src/main/java/com/tourandtravel/controller/AuthController.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tourandtravel.tourandtravelapplication.config.SecurityProperties;
import com.tourandtravel.tourandtravelapplication.model.User;
import com.tourandtravel.tourandtravelapplication.model.User.UserRole;
import com.tourandtravel.tourandtravelapplication.service.UserService;

@Controller
public class AuthController {
 
 @Autowired
 private UserService userService;
 
 @Autowired
 private SecurityProperties securityProperties;
 
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
 public String registerUser(@ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes, @RequestParam(name = "adminCode", required = false) String adminCode) {
     if (result.hasErrors()) {
         return "common/register";
     }
     
     try {
    	 user.setRole(UserRole.ROLE_USER);
         
    	 System.out.println("Checking admin code...");
         System.out.println("Admin code is: " + adminCode);
         System.out.println("Expected code is: " + securityProperties.getAdminSecretCode());
         
         // Only set ADMIN if a valid admin code is provided
    	 if (adminCode != null && !adminCode.isEmpty() && 
		    adminCode.equals(securityProperties.getAdminSecretCode())) {
		    user.setRole(UserRole.ROLE_ADMIN);
		    System.out.println("Admin role set successfully");
    	 } else if (user.getRole() == UserRole.ROLE_ADMIN) {
		    // If they tried to set ADMIN but provided no code or wrong code
		    System.out.println("Admin role requested but invalid code provided");
		    // Optionally add an error message here
		}
         userService.registerNewUser(user);
         redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
         return "redirect:/login";
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("error", e.getMessage());
         return "redirect:/register";
     }
 }
 
// @GetMapping("/dashboard")
// public String loginSuccess(Authentication authentication) {
//     Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//     boolean isAdmin = authorities.stream()
//         .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//         
//     if (isAdmin) {
//         return "redirect:/admin/dashboard";
//     } else {
//         return "redirect:/user/dashboard";
//     }
// }
}