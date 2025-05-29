package com.example.belajar_spring.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CartItem() {}

    public CartItem(Phone phone, User user) {
        this.phone = phone;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getSubtotal() {
        return phone != null ? phone.getPrice() : BigDecimal.ZERO;
    }
}
