package com.example.belajar_spring.controller;

import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.service.PhoneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping("/phones")
    public String viewPhones(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model) {

        Page<Phone> phonePage = phoneService.getPhones(keyword, sortDir, page, size);

        model.addAttribute("phones", phonePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phonePage.getTotalPages());
        model.addAttribute("totalItems", phonePage.getTotalElements());

        model.addAttribute("keyword", keyword);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "phones/index"; 
    }
}
