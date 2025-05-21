package com.example.belajar_spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Phone phone;

    private boolean sold = false;

    public CartItem(Phone phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public double getSubtotal() {
        return phone.getPrice();
    }

    public boolean isSold() {
        return sold;
    }
    
    public void setSold(boolean sold) {
        this.sold = sold;
    }
}
