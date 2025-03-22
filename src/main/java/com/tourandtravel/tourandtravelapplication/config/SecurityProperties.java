package com.tourandtravel.tourandtravelapplication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
	private String adminSecretCode;
	
	public String getAdminSecretCode() {
        return adminSecretCode;
    }

    public void setAdminSecretCode(String adminSecretCode) {
        this.adminSecretCode = adminSecretCode;
    }
}
