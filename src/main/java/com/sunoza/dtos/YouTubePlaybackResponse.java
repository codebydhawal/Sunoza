package com.sunoza.dtos;

public record YouTubePlaybackResponse(
        String videoId,
        String title,
        String channelTitle,
        String embedUrl
) {}
