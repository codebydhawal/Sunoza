package com.sunoza.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FavoriteArtistRequest(
        @NotBlank @Size(max = 128) String channelId,
        @NotBlank @Size(max = 255) String name,
        @Size(max = 1000) String thumbnailUrl
) {}
