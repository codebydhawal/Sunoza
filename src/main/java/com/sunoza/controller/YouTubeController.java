package com.sunoza.controller;

import com.sunoza.dtos.YouTubeSearchRequest;
import com.sunoza.dtos.YouTubeSearchResponse;
import com.sunoza.dtos.YouTubeVideoDetailsResponse;
import com.sunoza.dtos.YouTubePlaybackResponse;
import com.sunoza.service.GuestSearchLimitService;
import com.sunoza.service.YouTubeService;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/youtube")
public class YouTubeController {

    private final YouTubeService youtubeService;
    private final GuestSearchLimitService guestSearchLimitService;

    public YouTubeController(
            YouTubeService youtubeService,
            GuestSearchLimitService guestSearchLimitService
    ) {
        this.youtubeService = youtubeService;
        this.guestSearchLimitService = guestSearchLimitService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchVideos(
            @Valid @RequestBody YouTubeSearchRequest request,
            HttpServletRequest httpRequest
    ) {
        if (!isAuthenticated()) {
            int remaining = guestSearchLimitService.consumeSearch(httpRequest.getRemoteAddr());
            if (remaining < 0) {
                return ResponseEntity.status(429).body(Map.of(
                        "code", "GUEST_SEARCH_LIMIT_REACHED",
                        "message", "You have used all 5 guest searches for today. Sign in or create an account to keep searching.",
                        "limit", GuestSearchLimitService.DAILY_LIMIT,
                        "remaining", 0
                ));
            }

            YouTubeSearchResponse response = youtubeService.searchVideos(request);
            return ResponseEntity.ok()
                    .header("X-Guest-Search-Limit", String.valueOf(GuestSearchLimitService.DAILY_LIMIT))
                    .header("X-Guest-Searches-Remaining", String.valueOf(remaining))
                    .body(response);
        }

        return ResponseEntity.ok(youtubeService.searchVideos(request));
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/videos/{videoId}/playback")
    public ResponseEntity<?> getPlaybackInfo(@PathVariable String videoId) {
        if (!videoId.matches("[A-Za-z0-9_-]{11}")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid YouTube video ID."));
        }

        YouTubeVideoDetailsResponse details = youtubeService.getVideoDetails(videoId);
        if (details == null) return ResponseEntity.notFound().build();

        String embedUrl = "https://www.youtube-nocookie.com/embed/" + videoId + "?autoplay=1&playsinline=1&rel=0";
        return ResponseEntity.ok(new YouTubePlaybackResponse(
                details.getVideoId(), details.getTitle(), details.getChannelTitle(), embedUrl
        ));
    }

    @GetMapping("/videos")
    public ResponseEntity<YouTubeVideoDetailsResponse> getVideoDetails(
            @RequestParam String videoId
    ) {

        YouTubeVideoDetailsResponse response =
                youtubeService.getVideoDetails(videoId);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
