package com.example.belajar_spring.controller;

import com.example.belajar_spring.model.Transaction;
import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/transactions")
public class TransactionHistoryController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/history")
    public String viewHistory(
            @RequestParam(defaultValue = "transactionDate,desc") String sort,
            Model model, 
            HttpSession session) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Parse sort parameter
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";

        // Create Sort object
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                                 Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sorting = Sort.by(direction, sortField);

        // Get sorted transactions
        List<Transaction> transactions = transactionRepository.findByBuyerOrderByTransactionDateDesc(user, sorting);
        
        // Add current sort parameters to model
        model.addAttribute("currentSort", sortField);
        model.addAttribute("currentDirection", sortDirection);
        model.addAttribute("transactions", transactions);
        
        // Calculate total spent
        BigDecimal totalSpent = transactions.stream()
            .map(t -> t.getPhone().getPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalSpent", totalSpent);
        
        return "transactions/history";
    }
}