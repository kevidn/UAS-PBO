package com.example.belajar_spring.service;

import com.example.belajar_spring.model.Phone;
import com.example.belajar_spring.repository.PhoneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    public Page<Phone> getPhones(String keyword, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by("price").descending() 
            : Sort.by("price").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.trim().isEmpty()) {
            return phoneRepository.findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            return phoneRepository.findAll(pageable);
        }
    }
}
