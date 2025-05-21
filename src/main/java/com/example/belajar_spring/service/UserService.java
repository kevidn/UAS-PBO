package com.example.belajar_spring.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, String> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        // Tambahkan user default dengan password yang sudah di-hash
        users.put("admin", passwordEncoder.encode("admin123"));
        users.put("user", passwordEncoder.encode("user123"));
    }

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return false;
        }

        if (users.containsKey(username)) {
            return false; // Username sudah ada
        }

        String hashedPassword = passwordEncoder.encode(password);
        users.put(username, hashedPassword);
        return true;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String storedHashedPassword = users.get(username);

        return storedHashedPassword != null && passwordEncoder.matches(password, storedHashedPassword);
    }
}
