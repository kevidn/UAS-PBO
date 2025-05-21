package com.example.belajar_spring.controller;

import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.repository.PhoneRepository;
import com.example.belajar_spring.service.CartService;
import com.example.belajar_spring.service.PhoneService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhoneRepository cartRepository;

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("cartItems", cartService.getCartItems(session));
        return "cart/view";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Phone phone = phoneRepository.getPhoneById(id);
        if (phone != null && !phone.isSold()) {
            cartService.addToCart(session, phone);
            phone.setSold(true); // anggap 'sold' berarti sudah diambil
            phoneRepository.save(phone); // update ke database
            redirectAttributes.addFlashAttribute("success", "HP berhasil ditambahkan ke keranjang.");
        } else {
            redirectAttributes.addFlashAttribute("error", "HP sudah tidak tersedia.");
        }
        return "redirect:/phones";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(session, id);
        Phone phone = phoneRepository.getPhoneById(id);
        if (phone != null) {
            phone.setSold(false); // kembalikan jadi tersedia
            phoneRepository.save(phone);
        }
        redirectAttributes.addFlashAttribute("success", "HP berhasil dihapus dari keranjang.");
        return "redirect:/cart";
    }

}
