package com.sunoza.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PlaylistVideoRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9_-]{11}") String videoId,
        @NotBlank @Size(max = 500) String title,
        @Size(max = 255) String channelTitle,
        @Size(max = 1000) String thumbnailUrl
) {}
