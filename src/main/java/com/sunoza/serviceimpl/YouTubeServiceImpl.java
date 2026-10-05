package com.sunoza.serviceimpl;

import com.sunoza.dtos.*;
import com.sunoza.service.YouTubeService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.sunoza.utils.YouTubeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    private final RestClient restClient;

    @Value("${youtube.api.key}")
    private String apiKey;

    public YouTubeServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public YouTubeSearchResponse searchVideos(
            YouTubeSearchRequest request
    ) {

        /*
         * Call YouTube Search API
         */
        YouTubeApiResponse youtubeResponse =
                restClient.get()
                        .uri(uriBuilder -> {

                            uriBuilder
                                    .path("/search")
                                    .queryParam("part", "snippet")
                                    .queryParam("q", request.getQuery())
                                    .queryParam("type", "video")
                                    .queryParam(
                                            "maxResults",
                                            request.getMaxResults()
                                    )
                                    .queryParam("key", apiKey);

                            /*
                             * Pagination
                             */
                            if (request.getPageToken() != null
                                    && !request.getPageToken().isBlank()) {

                                uriBuilder.queryParam(
                                        "pageToken",
                                        request.getPageToken()
                                );
                            }

                            /*
                             * Sorting
                             */
                            if (request.getOrder() != null
                                    && !request.getOrder().isBlank()) {

                                uriBuilder.queryParam(
                                        "order",
                                        request.getOrder()
                                );
                            }

                            /*
                             * Published After
                             */
                            if (request.getPublishedAfter() != null
                                    && !request.getPublishedAfter().isBlank()) {

                                uriBuilder.queryParam(
                                        "publishedAfter",
                                        request.getPublishedAfter()
                                );
                            }

                            /*
                             * Published Before
                             */
                            if (request.getPublishedBefore() != null
                                    && !request.getPublishedBefore().isBlank()) {

                                uriBuilder.queryParam(
                                        "publishedBefore",
                                        request.getPublishedBefore()
                                );
                            }

                            /*
                             * Video Duration
                             */
                            if (request.getVideoDuration() != null
                                    && !request.getVideoDuration().isBlank()) {

                                uriBuilder.queryParam(
                                        "videoDuration",
                                        request.getVideoDuration()
                                );
                            }

                            return uriBuilder.build();
                        })
                        .retrieve()
                        .body(YouTubeApiResponse.class);

        /*
         * If YouTube returns no response
         */
        if (youtubeResponse == null) {

            return new YouTubeSearchResponse(
                    new ArrayList<>(),
                    null,
                    null,
                    0,
                    false,
                    false
            );
        }

        /*
         * Convert YouTube API items
         * into our application response
         */
        List<YouTubeVideoResponse> videos =
                new ArrayList<>();

        if (youtubeResponse.getItems() != null) {

            for (YouTubeApiResponse.Item item
                    : youtubeResponse.getItems()) {

                YouTubeApiResponse.Id id =
                        item.getId();

                YouTubeApiResponse.Snippet snippet =
                        item.getSnippet();

                /*
                 * Find thumbnail
                 */
                String thumbnailUrl = null;

                if (snippet != null
                        && snippet.getThumbnails() != null
                        && snippet.getThumbnails().getHigh() != null) {

                    thumbnailUrl =
                            snippet.getThumbnails()
                                    .getHigh()
                                    .getUrl();
                }

                /*
                 * Create our video response
                 */
                YouTubeVideoResponse video =
                        new YouTubeVideoResponse();

                if (id != null) {
                    video.setVideoId(
                            id.getVideoId()
                    );
                }

                if (snippet != null) {

                    video.setTitle(
                            snippet.getTitle()
                    );

                    video.setDescription(
                            snippet.getDescription()
                    );

                    video.setChannelId(
                            snippet.getChannelId()
                    );

                    video.setChannelTitle(
                            snippet.getChannelTitle()
                    );

                    video.setPublishedAt(
                            snippet.getPublishedAt()
                    );
                }

                video.setThumbnailUrl(
                        thumbnailUrl
                );

                videos.add(video);
            }
        }

        /*
         * Get total results
         */
        Integer totalResults = 0;

        if (youtubeResponse.getPageInfo() != null
                && youtubeResponse.getPageInfo().getTotalResults() != null) {

            totalResults =
                    youtubeResponse.getPageInfo()
                            .getTotalResults();
        }

        /*
         * Get pagination tokens
         */
        String nextPageToken =
                youtubeResponse.getNextPageToken();

        String prevPageToken =
                youtubeResponse.getPrevPageToken();

        /*
         * Determine pagination availability
         */
        boolean hasNextPage =
                nextPageToken != null
                        && !nextPageToken.isBlank();

        boolean hasPreviousPage =
                prevPageToken != null
                        && !prevPageToken.isBlank();

        /*
         * Return final response
         */
        return new YouTubeSearchResponse(
                videos,
                nextPageToken,
                prevPageToken,
                totalResults,
                hasNextPage,
                hasPreviousPage
        );
    }

    @Override
    public YouTubeVideoDetailsResponse getVideoDetails(
            String videoId
    ) {

        /*
         * Call YouTube Videos API
         */
        YouTubeVideoDetailsApiResponse youtubeResponse =
                restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/videos")
                                .queryParam(
                                        "part",
                                        "snippet,contentDetails,statistics"
                                )
                                .queryParam(
                                        "id",
                                        videoId
                                )
                                .queryParam(
                                        "key",
                                        apiKey
                                )
                                .build()
                        )
                        .retrieve()
                        .body(
                                YouTubeVideoDetailsApiResponse.class
                        );

        /*
         * If no response or video was not found
         */
        if (youtubeResponse == null
                || youtubeResponse.getItems() == null
                || youtubeResponse.getItems().isEmpty()) {

            return null;
        }

        /*
         * Get first video
         */
        YouTubeVideoDetailsApiResponse.Item item =
                youtubeResponse.getItems().get(0);

        /*
         * Get snippet
         */
        YouTubeVideoDetailsApiResponse.Snippet snippet =
                item.getSnippet();

        /*
         * Get content details
         */
        YouTubeVideoDetailsApiResponse.ContentDetails contentDetails =
                item.getContentDetails();

        /*
         * Get statistics
         */
        YouTubeVideoDetailsApiResponse.Statistics statistics =
                item.getStatistics();

        /*
         * Get thumbnail
         */
        String thumbnailUrl = null;

        if (snippet != null
                && snippet.getThumbnails() != null
                && snippet.getThumbnails().getHigh() != null) {

            thumbnailUrl =
                    snippet.getThumbnails()
                            .getHigh()
                            .getUrl();
        }

        /*
         * Create application response
         */
        YouTubeVideoDetailsResponse response =
                new YouTubeVideoDetailsResponse();

        /*
         * Video ID
         */
        response.setVideoId(videoId);

        /*
         * YouTube URL
         */
        response.setYoutubeUrl(
                "https://www.youtube.com/watch?v=" + videoId
        );

        /*
         * Embed URL
         */
        response.setEmbedUrl(
                "https://www.youtube.com/embed/" + videoId
        );

        /*
         * Snippet information
         */
        if (snippet != null) {

            response.setTitle(
                    snippet.getTitle()
            );

            response.setDescription(
                    snippet.getDescription()
            );

            response.setChannelId(
                    snippet.getChannelId()
            );

            response.setChannelTitle(
                    snippet.getChannelTitle()
            );

            response.setPublishedAt(
                    snippet.getPublishedAt()
            );
        }

        /*
         * Thumbnail
         */
        response.setThumbnailUrl(
                thumbnailUrl
        );

        /*
         * Duration
         */
        if (contentDetails != null) {

            response.setDuration(
                    YouTubeUtils.convertDuration(
                            contentDetails.getDuration()
                    )
            );
        }

        /*
         * Statistics
         */
        if (statistics != null) {

            response.setViewCount(
                    statistics.getViewCount()
            );

            response.setLikeCount(
                    statistics.getLikeCount()
            );

            response.setCommentCount(
                    statistics.getCommentCount()
            );
        }

        return response;
    }
}