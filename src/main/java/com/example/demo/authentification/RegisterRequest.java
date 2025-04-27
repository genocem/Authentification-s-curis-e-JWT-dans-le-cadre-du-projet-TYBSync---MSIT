package com.example.demo.authentification;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String role;
    @NotBlank
    private String password;
    @NotBlank
    private String email;


}
