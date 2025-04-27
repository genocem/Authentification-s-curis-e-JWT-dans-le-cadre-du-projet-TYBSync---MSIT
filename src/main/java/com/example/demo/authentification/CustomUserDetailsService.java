package com.example.demo.authentification;

import com.example.demo.AppUser.AppUser;
import com.example.demo.AppUser.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email).orElseThrow();

        return User.builder()
                .username(email)
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public AppUser findById(Long id) throws UsernameNotFoundException {
        return appUserRepository.findById(id).orElseThrow();
    }
}
