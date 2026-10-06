package com.sunoza.controller;

import com.sunoza.dtos.ApiResponse;
import com.sunoza.dtos.ArtistResponse;
import com.sunoza.dtos.FavoriteArtistRequest;
import com.sunoza.service.ArtistService;
import com.sunoza.service.YouTubeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/artists")
public class ArtistController {
    private final ArtistService artists;
    private final YouTubeService youtube;

    public ArtistController(ArtistService artists, YouTubeService youtube) {
        this.artists = artists;
        this.youtube = youtube;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ArtistResponse>>> search(@RequestParam String query) {
        if (query.isBlank() || query.length() > 100) throw new IllegalArgumentException("Enter an artist name to search.");
        return ResponseEntity.ok(ApiResponse.success(200, "Artists fetched successfully.", youtube.searchArtists(query.trim())));
    }

    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<FavoriteArtistRequest>>> favorites(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success(200, "Favorite artists fetched successfully.", artists.listFavorites(auth.getName())));
    }

    @PutMapping("/favorites")
    public ResponseEntity<ApiResponse<List<FavoriteArtistRequest>>> saveFavorites(Authentication auth,
            @Valid @RequestBody List<@Valid FavoriteArtistRequest> request) {
        return ResponseEntity.ok(ApiResponse.success(200, "Favorite artists saved successfully.", artists.replaceFavorites(auth.getName(), request)));
    }
}
