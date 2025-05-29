package com.example.belajar_spring.controller;

import com.example.belajar_spring.model.CartItem;
import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.PhoneRepository;
import com.example.belajar_spring.service.CartService;
import com.example.belajar_spring.service.PhoneService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    
    @Autowired
    private PhoneRepository phoneRepository;

    @GetMapping("")
    public String viewCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<CartItem> cartItems = cartService.getCartItems(user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", cartService.calculateTotal(user));
        return "cart/view";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Phone phone = phoneRepository.findById(id).orElse(null);
        if (phone != null && !phone.isSold()) {
            cartService.addToCart(user, phone);
            redirectAttributes.addFlashAttribute("success", "HP berhasil ditambahkan ke keranjang");
            return "redirect:/cart";
        }
        redirectAttributes.addFlashAttribute("error", "HP tidak tersedia");
        return "redirect:/phones";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        cartService.removeFromCart(user, id);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        cartService.processCheckout(user);
        return "redirect:/cart/success";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "cart/success";
    }
}
