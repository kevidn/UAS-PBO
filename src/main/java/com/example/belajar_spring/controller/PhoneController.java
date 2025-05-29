package com.example.belajar_spring.controller;

import com.example.belajar_spring.dto.PhoneForm;
import com.example.belajar_spring.model.CartItem;
import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.PhoneRepository;
import com.example.belajar_spring.service.CartService;
import com.example.belajar_spring.service.PhoneService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/phones")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneRepository phoneRepository;
    
    @Autowired
    private CartService cartService;

    @GetMapping("")
    public String viewPhones(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model,
            HttpSession session) {

        Page<Phone> phonePage = phoneService.getPhones(keyword, sortDir, page, size);

        model.addAttribute("phones", phonePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phonePage.getTotalPages());
        model.addAttribute("totalItems", phonePage.getTotalElements());

        model.addAttribute("keyword", keyword);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        // Ambil username dari session
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("currentUsername", user.getUsername());
        } else {
            model.addAttribute("currentUsername", null);
        }

        return "phones/index";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("phoneForm", new PhoneForm());
        return "phones/add";
    }

    @PostMapping("/save")
        public String savePhone(@ModelAttribute PhoneForm form, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        String username = user.getUsername();
        phoneService.savePhone(form, username);

        return "redirect:/phones";
    }

    @GetMapping("/{id}")
    public String detailPhone(@PathVariable Long id, Model model) {
        Phone phone = phoneRepository.getPhoneById(id);
        model.addAttribute("phone", phone);
        return "phones/detail"; 
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Phone phone = phoneRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid phone ID"));

        PhoneForm phoneForm = new PhoneForm();
        phoneForm.setId(phone.getId());
        phoneForm.setBrand(phone.getBrand());
        phoneForm.setModel(phone.getModel());
        phoneForm.setCondition(phone.getCondition());
        phoneForm.setPrice(phone.getPrice() != null ? phone.getPrice().doubleValue() : null);
        phoneForm.setDeskripsi(phone.getDeskripsi());
        // jangan set image, karena itu MultipartFile

        model.addAttribute("phoneForm", phoneForm);
        return "phones/edit";
    }


    @PostMapping("/update")
    public String updatePhone(@ModelAttribute("phoneForm") PhoneForm form) throws IOException {
        // cari phone berdasarkan id
        Phone phone = phoneRepository.findById(form.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid phone ID"));

        // update field
        phone.setBrand(form.getBrand());
        phone.setModel(form.getModel());
        phone.setCondition(form.getCondition());
        phone.setPrice(form.getPrice() != null ? BigDecimal.valueOf(form.getPrice()) : null);
        phone.setDeskripsi(form.getDeskripsi());

        // proses upload gambar jika ada file baru
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            String imageUrl = phoneService.storeImage(form.getImage()); // buat method simpan file di service
            phone.setImageUrl(imageUrl);
        }

        phoneRepository.save(phone);
        return "redirect:/phones";
    }
}
