package com.example.belajar_spring.controller;

import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.PhoneRepository;
import com.example.belajar_spring.repository.UserRepository;
import com.example.belajar_spring.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        long totalPhones = phoneRepository.count();
        long totalUsers = userRepository.count();
        long totalTransactions = transactionRepository.count();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("totalPhones", totalPhones);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTransactions", totalTransactions);

        return "dashboard";
    }
}
