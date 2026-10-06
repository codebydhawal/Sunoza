package com.sunoza.dtos;

import com.sunoza.model.PlaylistVideo;

public record PlaylistVideoResponse(
        String videoId,
        String title,
        String channelTitle,
        String thumbnailUrl
) {
    public static PlaylistVideoResponse from(PlaylistVideo video) {
        return new PlaylistVideoResponse(video.getVideoId(), video.getTitle(),
                video.getChannelTitle(), video.getThumbnailUrl());
    }
}
