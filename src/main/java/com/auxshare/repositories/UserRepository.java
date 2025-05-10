package com.auxshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auxshare.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
