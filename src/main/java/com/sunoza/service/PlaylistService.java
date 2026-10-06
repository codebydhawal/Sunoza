package com.sunoza.service;

import com.sunoza.dtos.CreatePlaylistRequest;
import com.sunoza.dtos.PlaylistResponse;
import com.sunoza.dtos.PlaylistVideoRequest;
import com.sunoza.model.Playlist;
import com.sunoza.model.PlaylistVideo;
import com.sunoza.model.User;
import com.sunoza.repository.PlaylistRepository;
import com.sunoza.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistService {
    private static final Pattern VIDEO_ID = Pattern.compile("^[A-Za-z0-9_-]{11}$");
    private final PlaylistRepository playlists;
    private final UserRepository users;

    public PlaylistService(PlaylistRepository playlists, UserRepository users) {
        this.playlists = playlists;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> list(String email) {
        User owner = owner(email);
        return playlists.findAllByOwner_IdOrderByCreatedAtDesc(owner.getId()).stream()
                .map(PlaylistResponse::from).toList();
    }

    @Transactional
    public PlaylistResponse create(String email, CreatePlaylistRequest request) {
        User owner = owner(email);
        String key = request.clientKey() == null || request.clientKey().isBlank() ? null : request.clientKey().trim();
        if (key != null) {
            var existing = playlists.findByOwner_IdAndClientKey(owner.getId(), key);
            if (existing.isPresent()) return PlaylistResponse.from(existing.get());
        }
        return PlaylistResponse.from(playlists.save(new Playlist(owner, request.name().trim(), key)));
    }

    @Transactional
    public PlaylistResponse addVideo(String email, Long playlistId, PlaylistVideoRequest request) {
        Playlist playlist = ownedPlaylist(email, playlistId);
        if (!VIDEO_ID.matcher(request.videoId()).matches()) throw new IllegalArgumentException("Invalid YouTube video ID.");
        if (playlist.getVideos().stream().noneMatch(v -> v.getVideoId().equals(request.videoId()))) {
            playlist.getVideos().add(new PlaylistVideo(playlist, request.videoId(), request.title().trim(),
                    request.channelTitle(), request.thumbnailUrl(), playlist.getVideos().size()));
        }
        return PlaylistResponse.from(playlists.save(playlist));
    }

    @Transactional
    public PlaylistResponse removeVideo(String email, Long playlistId, String videoId) {
        Playlist playlist = ownedPlaylist(email, playlistId);
        playlist.getVideos().removeIf(video -> video.getVideoId().equals(videoId));
        for (int index = 0; index < playlist.getVideos().size(); index++) playlist.getVideos().get(index).setPosition(index);
        return PlaylistResponse.from(playlists.save(playlist));
    }

    @Transactional
    public void delete(String email, Long playlistId) {
        playlists.delete(ownedPlaylist(email, playlistId));
    }

    private Playlist ownedPlaylist(String email, Long id) {
        User owner = owner(email);
        return playlists.findByIdAndOwner_Id(id, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Playlist not found."));
    }

    private User owner(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new NoSuchElementException("User not found."));
    }
}
