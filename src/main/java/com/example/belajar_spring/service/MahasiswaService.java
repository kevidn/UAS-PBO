package com.example.belajar_spring.service;

import com.example.belajar_spring.model.Jurusan;
import com.example.belajar_spring.model.Mahasiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class MahasiswaService {
    private List<Mahasiswa> mahasiswaList = new ArrayList<>();
    
    @Autowired
    private JurusanService jurusanService;

    public MahasiswaService() {
        // Data awal akan ditambahkan setelah konstruktor selesai
    }

    @PostConstruct
    public void init() {
        // Data awal mahasiswa
        List<Jurusan> jurusanList = jurusanService.getAllJurusan();
        if (!jurusanList.isEmpty()) {
            mahasiswaList.add(new Mahasiswa(1L, "Wahyu", jurusanList.get(0))); // TI
            mahasiswaList.add(new Mahasiswa(2L, "Andi", jurusanList.get(1)));  // SI
        }
    }

    // CRUD Mahasiswa
    public List<Mahasiswa> getAllMahasiswa() {
        return mahasiswaList;
    }

    public Mahasiswa getMahasiswaById(Long id) {
        return mahasiswaList.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public void addMahasiswa(Mahasiswa mahasiswa) {
        mahasiswaList.add(mahasiswa);
    }

    public void updateMahasiswa(Mahasiswa mahasiswa) {
        mahasiswaList.replaceAll(m -> m.getId().equals(mahasiswa.getId()) ? mahasiswa : m);
    }

    public void deleteMahasiswa(Long id) {
        mahasiswaList.removeIf(m -> m.getId().equals(id));
    }

    // Data Jurusan diambil dari JurusanService
    public List<Jurusan> getAllJurusan() {
        return jurusanService.getAllJurusan();
    }

    public Jurusan getJurusanById(Long id) {
        return jurusanService.getJurusanByID(id);
    }
}