package com.sunoza.controller;

import com.sunoza.dtos.ApiResponse;
import com.sunoza.dtos.CreatePlaylistRequest;
import com.sunoza.dtos.PlaylistResponse;
import com.sunoza.dtos.PlaylistVideoRequest;
import com.sunoza.service.PlaylistService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/playlists")
public class PlaylistController {
    private final PlaylistService service;

    public PlaylistController(PlaylistService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlaylistResponse>>> list(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success(200, "Playlists fetched successfully.", service.list(auth.getName())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlaylistResponse>> create(Authentication auth, @Valid @RequestBody CreatePlaylistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Playlist created successfully.", service.create(auth.getName(), request)));
    }

    @PostMapping("/{playlistId}/videos")
    public ResponseEntity<ApiResponse<PlaylistResponse>> addVideo(Authentication auth, @PathVariable Long playlistId,
                                                                    @Valid @RequestBody PlaylistVideoRequest request) {
        return ResponseEntity.ok(ApiResponse.success(200, "Video added to playlist.", service.addVideo(auth.getName(), playlistId, request)));
    }

    @DeleteMapping("/{playlistId}/videos/{videoId}")
    public ResponseEntity<ApiResponse<PlaylistResponse>> removeVideo(Authentication auth, @PathVariable Long playlistId,
                                                                       @PathVariable String videoId) {
        return ResponseEntity.ok(ApiResponse.success(200, "Video removed from playlist.", service.removeVideo(auth.getName(), playlistId, videoId)));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(Authentication auth, @PathVariable Long playlistId) {
        service.delete(auth.getName(), playlistId);
        return ResponseEntity.ok(ApiResponse.success(200, "Playlist deleted successfully.", Map.of("status", "deleted")));
    }
}
