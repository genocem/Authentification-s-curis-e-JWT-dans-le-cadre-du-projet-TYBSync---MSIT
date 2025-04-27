package com.example.demo.AppUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAppUser(AppUser newUser) {
        if (appUserRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalArgumentException("CIN already exists");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        appUserRepository.save(newUser);
    }
    public void createAppUser(String name, String role, String password,String email) {
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("CIN already exists");
        }
        if(appUserRepository.existsByEmail(email)){
            throw new IllegalArgumentException("Email already exists");
        }
        AppUser newUser = new AppUser(name, role,email);
        newUser.setPassword(passwordEncoder.encode(password));
        appUserRepository.save(newUser);
    }

    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser with id " + id + " not found"));
    }





    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("AppUser with email " + email + " not found"));

    }

    public boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }
}
