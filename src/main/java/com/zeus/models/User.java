package com.zeus.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Song> songs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes;


    // OBS: Endast en tom konstruktor för JPA
    public User() {}

    // Konstruktor som kräver både username och password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }
}
