package com.example.belajar_spring.service;

import com.example.belajar_spring.model.CartItem;
import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.model.Transaction;
import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.CartItemRepository;
import com.example.belajar_spring.repository.PhoneRepository;
import com.example.belajar_spring.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CartService {
    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addToCart(User user, Phone phone) {
        // Check if phone is already in anyone's cart
        if (!phone.isSold()) {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setPhone(phone);
            cartItem.setUser(user);
            
            // Save cart item
            cartItemRepository.save(cartItem);
            
            // Update phone status
            phone.setSold(true);
            phoneRepository.save(phone);
        }
    }

    public void removeFromCart(User user, Long phoneId) {
        Phone phone = phoneRepository.findById(phoneId).orElse(null);
        if (phone != null) {
            // Find cart item for this user and phone
            CartItem cartItem = cartItemRepository.findByUserAndPhone(user, phone);
            
            if (cartItem != null) {
                // Remove from cart
                cartItemRepository.delete(cartItem);
                
                // Update phone status
                phone.setSold(false);
                phoneRepository.save(phone);
            }
        }
    }

    public BigDecimal calculateTotal(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return cartItems.stream()
                .map(item -> item.getPhone().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void processCheckout(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        
        // Create transactions for each phone
        for (CartItem item : cartItems) {
            Phone phone = item.getPhone();
            
            // Create new transaction
            Transaction transaction = new Transaction();
            transaction.setPhone(phone);
            transaction.setBuyer(user);
            transaction.setTransactionDate(LocalDateTime.now());
            
            // Save transaction first
            transactionRepository.save(transaction);
            
            // Then delete cart item
            cartItemRepository.delete(item);
            
            // Finally delete phone
            phoneRepository.delete(phone);
        }
    }
}
