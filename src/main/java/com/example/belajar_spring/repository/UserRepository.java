package com.example.belajar_spring.repository;

import com.example.belajar_spring.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);
}
