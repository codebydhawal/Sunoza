package com.sunoza.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePlaylistRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 80) String clientKey
) {}
