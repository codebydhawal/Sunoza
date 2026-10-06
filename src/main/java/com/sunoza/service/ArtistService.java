package com.sunoza.service;

import com.sunoza.dtos.ArtistResponse;
import com.sunoza.dtos.FavoriteArtistRequest;
import com.sunoza.model.FavoriteArtist;
import com.sunoza.model.User;
import com.sunoza.repository.FavoriteArtistRepository;
import com.sunoza.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistService {
    private final FavoriteArtistRepository favorites;
    private final UserRepository users;

    public ArtistService(FavoriteArtistRepository favorites, UserRepository users) {
        this.favorites = favorites;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<FavoriteArtistRequest> listFavorites(String email) {
        User owner = owner(email);
        return favorites.findAllByOwner_IdOrderByNameAsc(owner.getId()).stream()
                .filter(artist -> artist.getName() != null && artist.getName().toLowerCase(java.util.Locale.ROOT).endsWith(" - topic"))
                .map(artist -> new FavoriteArtistRequest(artist.getChannelId(), artist.getName(), artist.getThumbnailUrl()))
                .toList();
    }

    @Transactional
    public List<FavoriteArtistRequest> replaceFavorites(String email, List<FavoriteArtistRequest> artists) {
        if (artists.size() > 30) throw new IllegalArgumentException("Choose up to 30 favorite singers.");
        if (artists.stream().anyMatch(artist -> artist.name() == null || !artist.name().trim().toLowerCase(java.util.Locale.ROOT).endsWith(" - topic"))) {
            throw new IllegalArgumentException("Only singers with a YouTube Topic channel can be saved.");
        }
        User owner = owner(email);
        favorites.deleteAllByOwner_Id(owner.getId());
        List<FavoriteArtist> saved = artists.stream()
                .collect(java.util.stream.Collectors.toMap(FavoriteArtistRequest::channelId, artist -> artist, (first, ignored) -> first))
                .values().stream()
                .map(artist -> new FavoriteArtist(owner, artist.channelId().trim(), artist.name().trim(), artist.thumbnailUrl()))
                .toList();
        return favorites.saveAll(saved).stream()
                .map(artist -> new FavoriteArtistRequest(artist.getChannelId(), artist.getName(), artist.getThumbnailUrl()))
                .toList();
    }

    private User owner(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new NoSuchElementException("User not found."));
    }
}
