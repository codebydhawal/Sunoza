package com.sunoza.dtos;
import jakarta.validation.constraints.*;
public record UpdateUserRequest(@NotBlank @Size(max=120) String name, @NotBlank @Email @Size(max=190) String email) {}
