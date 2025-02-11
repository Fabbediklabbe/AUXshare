package com.zeus.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column
    private String link; // Länk till låten (t.ex. YouTube, Spotify)

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false) // Kopplar till users-tabellen
    @JsonIgnore // Hindrar att hela User-objektet serialiseras
    private User user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addedAt;

    public Song() {}

    public Song(String title, String artist, String link, User user, LocalDateTime addedAt) {
        this.title = title;
        this.artist = artist;
        this.link = link;
        this.user = user;
        this.addedAt = addedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    // För att automatiskt inkludera användarnamnet i JSON-svaret
    @JsonProperty("addedBy")
    public String getAddedByUsername() {
        return user != null ? user.getUsername() : "Okänd";
    }
}
