package com.tourandtravel.tourandtravelapplication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityCodeService {
    @Value("${app.security.admin-secret-code}")
    private String adminSecretCode;
    
    public boolean isValidAdminCode(String code) {
        return code != null && code.equals(adminSecretCode);
    }
}