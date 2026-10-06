package com.sunoza.dtos;

import com.sunoza.model.Playlist;
import java.time.Instant;
import java.util.List;

public record PlaylistResponse(
        Long id,
        String name,
        String clientKey,
        Instant createdAt,
        List<PlaylistVideoResponse> videos
) {
    public static PlaylistResponse from(Playlist playlist) {
        return new PlaylistResponse(playlist.getId(), playlist.getName(), playlist.getClientKey(),
                playlist.getCreatedAt(), playlist.getVideos().stream().map(PlaylistVideoResponse::from).toList());
    }
}
