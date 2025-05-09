package com.zeus.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zeus.models.Like;
import com.zeus.models.Song;
import com.zeus.models.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndSong(User user, Song song);
    List<Like> findBySong(Song song);
    Optional<Like> findByUserAndSong(User user, Song song);
}
