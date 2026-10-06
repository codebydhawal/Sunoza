package com.sunoza.service;

import com.sunoza.dtos.YouTubeSearchRequest;
import com.sunoza.dtos.YouTubeSearchResponse;
import com.sunoza.dtos.YouTubeVideoDetailsResponse;
import com.sunoza.dtos.ArtistResponse;
import java.util.List;

public interface YouTubeService {

    YouTubeSearchResponse searchVideos(
            YouTubeSearchRequest request
    );

    YouTubeVideoDetailsResponse getVideoDetails(
            String videoId
    );

    List<ArtistResponse> searchArtists(String query);
}
