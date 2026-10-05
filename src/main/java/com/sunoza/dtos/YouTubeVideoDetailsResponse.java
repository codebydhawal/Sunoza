package com.sunoza.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoDetailsResponse {

    private String videoId;

    private String title;

    private String description;

    private String channelId;

    private String channelTitle;

    private String publishedAt;

    private String thumbnailUrl;

    private String duration;

    private Long viewCount;

    private Long likeCount;

    private Long commentCount;

    private String youtubeUrl;

    private String embedUrl;
}