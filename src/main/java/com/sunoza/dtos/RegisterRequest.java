package com.sunoza.dtos;
import jakarta.validation.constraints.*;
public record RegisterRequest(
        @NotBlank @Size(max=120) String name,
        @NotBlank @Email @Size(max=190) String email,
        @NotBlank @Size(min=8,max=72) String password,
        @NotBlank @Pattern(regexp = "(?i)USER|ADMIN", message = "Role must be USER or ADMIN") String role
) {}
