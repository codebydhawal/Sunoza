package com.sunoza.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchRequest {

    @NotBlank(message = "Search query is required")
    private String query;

    @Min(value = 1, message = "maxResults must be at least 1")
    @Max(value = 50, message = "maxResults cannot be greater than 50")
    private Integer maxResults = 10;

    private String pageToken;

    /*
     * Allowed:
     * relevance
     * date
     * rating
     * viewCount
     * title
     */
    @Pattern(
            regexp = "relevance|date|rating|viewCount|title",
            message = "Invalid order. Allowed values: relevance, date, rating, viewCount, title"
    )
    private String order;

    private String publishedAfter;

    private String publishedBefore;

    /*
     * Allowed:
     * short
     * medium
     * long
     */
    @Pattern(
            regexp = "short|medium|long",
            message = "Invalid videoDuration. Allowed values: short, medium, long"
    )
    private String videoDuration;
}