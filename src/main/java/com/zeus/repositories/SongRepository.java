package com.zeus.repositories;

import com.zeus.models.Song;
import com.zeus.models.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByUser(User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Song s WHERE s.id = :id")
    void deleteSongById(Long id);
}
