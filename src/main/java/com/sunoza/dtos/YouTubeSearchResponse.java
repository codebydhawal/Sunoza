package com.sunoza.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchResponse {

    private List<YouTubeVideoResponse> videos;

    private String nextPageToken;

    private String prevPageToken;

    private Integer totalResults;

    private boolean hasNextPage;

    private boolean hasPreviousPage;
}