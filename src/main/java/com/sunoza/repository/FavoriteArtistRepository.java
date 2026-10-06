package com.sunoza.repository;

import com.sunoza.model.FavoriteArtist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {
    List<FavoriteArtist> findAllByOwner_IdOrderByNameAsc(Long ownerId);
    void deleteAllByOwner_Id(Long ownerId);
}
