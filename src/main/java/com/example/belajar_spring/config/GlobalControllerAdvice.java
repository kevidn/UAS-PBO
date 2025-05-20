package com.example.belajar_spring.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
    }
}
