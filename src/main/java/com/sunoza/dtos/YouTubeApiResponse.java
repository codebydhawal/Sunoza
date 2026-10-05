package com.sunoza.dtos;

import lombok.Data;

import java.util.List;

@Data
public class YouTubeApiResponse {

    private String nextPageToken;

    private String prevPageToken;

    private PageInfo pageInfo;

    private List<Item> items;

    @Data
    public static class PageInfo {

        private Integer totalResults;

        private Integer resultsPerPage;
    }

    @Data
    public static class Item {

        private Id id;

        private Snippet snippet;
    }

    @Data
    public static class Id {

        private String kind;

        private String videoId;
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