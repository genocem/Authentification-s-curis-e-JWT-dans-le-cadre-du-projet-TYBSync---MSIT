package com.example.demo;

import com.example.demo.AppUser.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(AppUserService appUserService) {
        return args -> {
            try {
                appUserService.createAppUser("admin", "ADMIN", "123", "admin@admin.com");
                log.info("Created default admin with CIN 11");
            } catch (IllegalArgumentException e) {
                log.info("Admin with CIN 11 already exists");
            }
        };
    }
}
