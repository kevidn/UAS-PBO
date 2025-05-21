package com.example.belajar_spring.controller;

import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user); // Harus cocok dengan LoginInterceptor
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Username atau password salah");
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register"; // view register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Registrasi berhasil! Silakan login.");
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
