package com.sunoza.repository;

import com.sunoza.model.Playlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByOwner_IdOrderByCreatedAtDesc(Long ownerId);
    Optional<Playlist> findByIdAndOwner_Id(Long id, Long ownerId);
    Optional<Playlist> findByOwner_IdAndClientKey(Long ownerId, String clientKey);
}
