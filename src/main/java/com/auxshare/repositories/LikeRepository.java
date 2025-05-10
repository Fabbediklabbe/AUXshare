package com.auxshare.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auxshare.models.Like;
import com.auxshare.models.Song;
import com.auxshare.models.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndSong(User user, Song song);
    List<Like> findBySong(Song song);
    Optional<Like> findByUserAndSong(User user, Song song);
}
