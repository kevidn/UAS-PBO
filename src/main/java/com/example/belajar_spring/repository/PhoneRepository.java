package com.example.belajar_spring.repository;

import com.example.belajar_spring.model.Phone;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Page<Phone> findAll(Pageable pageable);

    Page<Phone> findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(String keyword, String keyword2,
            Pageable pageable);

    @Query("SELECT p FROM Phone p WHERE LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.model) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY p.price ASC")
    List<Phone> getAllPhones(@Param("keyword") String keyword);

    Optional<Phone> findById(Long id);
    
    List<Phone> findAll();
    
    void deleteById(Long id);

    // Method default untuk mengambil Phone by ID
    default Phone getPhoneById(Long id) {
        return findById(id).orElse(null);
    }
}

