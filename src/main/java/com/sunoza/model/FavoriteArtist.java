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
@Table(name = "favorite_artists", uniqueConstraints =
        @UniqueConstraint(name = "uk_favorite_artist_owner_channel", columnNames = {"owner_id", "channel_id"}))
public class FavoriteArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "channel_id", nullable = false, length = 128)
    private String channelId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    protected FavoriteArtist() {}

    public FavoriteArtist(User owner, String channelId, String name, String thumbnailUrl) {
        this.owner = owner;
        this.channelId = channelId;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getId() { return id; }
    public User getOwner() { return owner; }
    public String getChannelId() { return channelId; }
    public String getName() { return name; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}
