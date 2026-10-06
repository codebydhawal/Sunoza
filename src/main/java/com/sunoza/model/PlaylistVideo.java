package com.sunoza.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "playlist_videos", uniqueConstraints =
        @UniqueConstraint(name = "uk_playlist_video_id", columnNames = {"playlist_id", "video_id"}))
public class PlaylistVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @Column(name = "video_id", nullable = false, length = 11)
    private String videoId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "channel_title", length = 255)
    private String channelTitle;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @Column(nullable = false)
    private int position;

    protected PlaylistVideo() {}

    public PlaylistVideo(Playlist playlist, String videoId, String title, String channelTitle,
                         String thumbnailUrl, int position) {
        this.playlist = playlist;
        this.videoId = videoId;
        this.title = title;
        this.channelTitle = channelTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.position = position;
    }

    public String getVideoId() { return videoId; }
    public String getTitle() { return title; }
    public String getChannelTitle() { return channelTitle; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}
