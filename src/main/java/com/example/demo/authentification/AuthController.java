package com.example.demo.authentification;

import com.example.demo.AppUser.AppUser;
import com.example.demo.AppUser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserService appUserService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.appUserService = appUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            String email = request.getEmail();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            AppUser appUser = appUserService.findByEmail(email);
            String token = jwtTokenUtil.generateToken(String.valueOf(appUser.getId()));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            String name=request.getName();
            String role=request.getRole();

            if (appUserService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already in use"));
            }

            AppUser newappUser = new AppUser(name,role, email);
            newappUser.setPassword(password);
            newappUser.setName(request.getName() != null ? request.getName() : email);
            newappUser.setRole("USER");

            appUserService.createAppUser(newappUser);

            String token = jwtTokenUtil.generateToken(String.valueOf(newappUser.getId()));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("token", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }


    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }

            String token = authorizationHeader.substring(7);
            if (!jwtTokenUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired token"));
            }

            Long id = Long.parseLong(jwtTokenUtil.extractId(token));
            var appUser = appUserService.findById(id);

            return ResponseEntity.ok(Map.of(
                    "message", "Token is valid",
                    "email", appUser.getEmail() ,
                    "username", appUser.getName(),
                    "roles", appUser.getRole()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred: " + e.getMessage()));
        }
    }
}
