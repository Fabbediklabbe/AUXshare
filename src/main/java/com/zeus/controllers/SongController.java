package com.zeus.controllers;

import com.zeus.models.Like;
import com.zeus.models.Song;
import com.zeus.models.User;
import com.zeus.repositories.LikeRepository;
import com.zeus.repositories.SongRepository;
import com.zeus.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/songs")
@Transactional // Säkerställer att transaktioner utförs korrekt
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public SongController(SongRepository songRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSongs() {
        logger.info("Fetching all songs");
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

            List<Like> likes = likeRepository.findBySong(song);
            List<String> likers = likes.stream()
                .map((Like like) -> like.getUser().getUsername())
                .toList();

            songMap.put("likes", likers);

            return songMap;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Song> addSong(@RequestBody Song song, @RequestParam String username) {
        logger.info("Trying to add song by user: {}", username);
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

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeSong(@PathVariable Long id, Principal principal) {
        Optional<Song> songOpt = songRepository.findById(id);
        if (songOpt.isEmpty()) return ResponseEntity.notFound().build();

        Song song = songOpt.get();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        if (likeRepository.existsByUserAndSong(user, song)) {
            return ResponseEntity.badRequest().body("Already liked");
        }

        Like like = new Like(user, song);
        likeRepository.save(like);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlikeSong(@PathVariable Long id, Principal principal) {
        Optional<Song> songOpt = songRepository.findById(id);
        if (songOpt.isEmpty()) return ResponseEntity.notFound().build();

        Song song = songOpt.get();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        Optional<Like> likeOpt = likeRepository.findByUserAndSong(user, song);
        if (likeOpt.isPresent()) {
            likeRepository.delete(likeOpt.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingen like att ta bort");
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id, Principal principal) {
        logger.info("User {} trying to remove song with ID {}", principal.getName(), id);
        Optional<Song> songOpt = songRepository.findById(id);
        if (songOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Song song = songOpt.get();

        // Kontrollera att det är användaren som äger låten
        if (!song.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        songRepository.delete(song);
        return ResponseEntity.ok().build();
    }
}
