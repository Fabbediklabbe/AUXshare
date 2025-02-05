package com.zeus.repositories;

import com.zeus.models.Song;
import com.zeus.models.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    
    // Hämta alla låtar i fallande ordning baserat på ID
    @Query("SELECT s FROM Song s ORDER BY s.id DESC")
    List<Song> findAllOrderByIdDesc();

    // Hämta låtar för en specifik användare i fallande ordning
    @Query("SELECT s FROM Song s WHERE s.user = :user ORDER BY s.id DESC")
    List<Song> findByUserOrderByIdDesc(@Param("user") User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Song s WHERE s.id = :id")
    void deleteSongById(Long id);
}
