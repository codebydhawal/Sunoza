package com.sunoza.dtos;

import lombok.Data;

import java.util.List;

@Data
public class YouTubeVideoDetailsApiResponse {

    private List<Item> items;

    @Data
    public static class Item {

        private String id;

        private Snippet snippet;

        private ContentDetails contentDetails;

        private Statistics statistics;
    }

    @Data
    public static class Snippet {

        private String publishedAt;

        private String channelId;

        private String title;

        private String description;

        private Thumbnails thumbnails;

        private String channelTitle;
    }

    @Data
    public static class ContentDetails {

        private String duration;
    }

    @Data
    public static class Statistics {

        private Long viewCount;

        private Long likeCount;

        private Long commentCount;
    }

    @Data
    public static class Thumbnails {

        private Thumbnail defaultThumbnail;

        private Thumbnail medium;

        private Thumbnail high;
    }

    @Data
    public static class Thumbnail {

        private String url;

        private Integer width;

        private Integer height;
    }
}