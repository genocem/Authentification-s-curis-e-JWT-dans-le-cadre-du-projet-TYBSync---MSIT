package com.example.demo.authentification;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegistrationRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String role;

    @NotBlank
    private String password;



}
