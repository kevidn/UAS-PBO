package com.example.belajar_spring.repository;

import com.example.belajar_spring.model.CartItem;
import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
	CartItem findByUserAndPhone(User user, Phone phone);
}