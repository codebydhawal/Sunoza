package com.sunoza.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoResponse {

    private String videoId;

    private String title;

    private String description;

    private String channelId;

    private String channelTitle;

    private String publishedAt;

    private String thumbnailUrl;
}