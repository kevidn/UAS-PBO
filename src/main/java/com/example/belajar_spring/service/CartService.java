package com.example.belajar_spring.service;

import com.example.belajar_spring.model.CartItem;
import com.example.belajar_spring.model.Phone;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public void addToCart(HttpSession session, Phone phone) {
        // Ambil cart dari session atau buat baru
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartItems", cartItems);
        }

        // Cek apakah sudah ada di keranjang
        boolean alreadyInCart = cartItems.stream()
                .anyMatch(item -> item.getPhone().getId().equals(phone.getId()));

        if (!alreadyInCart) {
            cartItems.add(new CartItem(phone));
        }
    }

    @SuppressWarnings("unchecked")
    public List<CartItem> getCartItems(HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartItems", cartItems);
        }
        return cartItems;
    }


    public double getTotalPrice() {
        return cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void removeFromCart(HttpSession session, Long phoneId) {
        List<CartItem> cartItems = getCartItems(session);
        cartItems.removeIf(item -> item.getPhone().getId().equals(phoneId));
        session.setAttribute("cartItems", cartItems);
    }
}
