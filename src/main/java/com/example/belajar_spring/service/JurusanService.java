package com.example.belajar_spring.service;

import com.example.belajar_spring.model.Jurusan;
import com.example.belajar_spring.model.Mahasiswa;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JurusanService {
    private final List<Jurusan> jurusanList = new ArrayList<>();
    private Long idCounter = 1L;

    public JurusanService() {
        // Menambahkan data awal jurusan
        saveJurusan(new Jurusan(null, "Teknik Informatika"));
        saveJurusan(new Jurusan(null, "Sistem Informasi"));
        saveJurusan(new Jurusan(null, "Teknik Komputer"));
    }

    public List<Jurusan> getAllJurusan() {
        return jurusanList;
    }

    public Jurusan saveJurusan(Jurusan jurusan) {
        if (jurusan.getId() == null) {
            jurusan.setId(idCounter++);
            jurusanList.add(jurusan);
        }
        return jurusan;
    }

    public Jurusan getJurusanByID(Long id) {
        return jurusanList.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public void updateJurusan(Jurusan jurusan) {
        jurusanList.replaceAll(m -> m.getId().equals(jurusan.getId()) ? jurusan : m);
    }
}