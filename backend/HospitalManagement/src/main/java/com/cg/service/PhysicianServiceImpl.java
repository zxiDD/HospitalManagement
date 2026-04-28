package com.cg.service;

import com.cg.entity.Physician;
import com.cg.repo.PhysicianRepository;
import com.cg.service.PhysicianService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicianServiceImpl implements PhysicianService {

    private final PhysicianRepository repo;

    public PhysicianServiceImpl(PhysicianRepository repo) {
        this.repo = repo;
    }

    public List<Physician> getAll() {
        return repo.findAll();
    }

    public Physician getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Physician not found"));
    }

    public Page<Physician> getAllPaged(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    public List<Physician> getAllSorted() {
        return repo.findAll(Sort.by("name"));
    }

    public boolean exists(Integer id) {
        return repo.existsById(id);
    }

    public long count() {
        return repo.count();
    }
    
    @Override
    public Physician save(Physician physician) {
        return repo.save(physician);
    }
}