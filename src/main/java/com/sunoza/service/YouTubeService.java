package com.sunoza.service;

import com.sunoza.dtos.YouTubeSearchRequest;
import com.sunoza.dtos.YouTubeSearchResponse;
import com.sunoza.dtos.YouTubeVideoDetailsResponse;

public interface YouTubeService {

    YouTubeSearchResponse searchVideos(
            YouTubeSearchRequest request
    );

    YouTubeVideoDetailsResponse getVideoDetails(
            String videoId
    );
}