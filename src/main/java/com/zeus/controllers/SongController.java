package com.zeus.controllers;

import com.zeus.models.Song;
import com.zeus.models.User;
import com.zeus.repositories.SongRepository;
import com.zeus.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/songs")
@Transactional // Säkerställer att transaktioner utförs korrekt
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public SongController(SongRepository songRepository, UserRepository userRepository) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSongs() {
        List<Song> songs = songRepository.findAllOrderByIdDesc();

        List<Map<String, Object>> response = songs.stream().map(song -> {
            Map<String, Object> songMap = new HashMap<>();
            songMap.put("id", song.getId());
            songMap.put("title", song.getTitle());
            songMap.put("artist", song.getArtist());
            songMap.put("link", song.getLink());
            songMap.put("addedAt", song.getAddedAt());

            // Använd `user_id` för att direkt hämta användarnamnet
            songMap.put("username", (song.getUser() != null) ? song.getUser().getUsername() : "Okänd");

            return songMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Song> addSong(@RequestBody Song song, @RequestParam String username) {
        if (song.getTitle() == null || song.getTitle().trim().isEmpty() ||
                song.getArtist() == null || song.getArtist().trim().isEmpty() ||
                song.getLink() == null || song.getLink().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        User user = existingUser.get();


        song.setUser(user);
        song.setAddedAt(LocalDateTime.now());

        Song savedSong = songRepository.save(song);
        return ResponseEntity.ok(savedSong);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (songRepository.existsById(id)) {
            System.out.println("Försöker radera låt med ID: " + id);

            songRepository.deleteSongById(id); // Använd vår anpassade metod

            System.out.println("Låten raderades! Kontrollera MySQL.");
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Låten med ID " + id + " fanns inte i databasen.");
            return ResponseEntity.notFound().build();
        }

    }

    
}
