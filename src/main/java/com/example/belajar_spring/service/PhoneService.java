package com.example.belajar_spring.service;

import com.example.belajar_spring.dto.PhoneForm;
import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.model.User;
import com.example.belajar_spring.repository.PhoneRepository;
import com.example.belajar_spring.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePhone(PhoneForm form, String username) throws IOException {
        Phone phone = new Phone();
        phone.setBrand(form.getBrand());
        phone.setModel(form.getModel());
        phone.setCondition(form.getCondition());
        phone.setPrice(form.getPrice());
        phone.setDeskripsi(form.getDeskripsi());

        // Simulasi penyimpanan image ke direktori lokal
        if (!form.getImage().isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + form.getImage().getOriginalFilename();
            String uploadDir = "uploads";
            
            Files.createDirectories(Paths.get(uploadDir));
            Path imagePath = Paths.get(uploadDir, fileName);
            Files.write(imagePath, form.getImage().getBytes());
            phone.setImageUrl("/uploads/" + fileName);
        }

        User seller = userRepository.findByUsername(username).orElseThrow();
        phone.setSeller(seller);

        phoneRepository.save(phone);
    }

    public Page<Phone> getPhones(String keyword, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by("price").ascending() : Sort.by("price").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return phoneRepository.findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            return phoneRepository.findAll(pageable);
        }
    }

    public String storeImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/"); // folder upload misal di resources/static/uploads
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return "/uploads/" + filename; // return url path untuk disimpan di database
    }

    public void deletePhone(Long id) {
        phoneRepository.deleteById(id);
    }
}
