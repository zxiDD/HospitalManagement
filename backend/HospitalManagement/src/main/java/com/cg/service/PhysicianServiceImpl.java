package com.cg.service;

import com.cg.entity.Physician;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.BadRequestException;
import com.cg.repo.PhysicianRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicianServiceImpl implements PhysicianService {

    private final PhysicianRepository repo;

    public PhysicianServiceImpl(PhysicianRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Physician> getAll() {
        return repo.findByIsActiveTrue();
    }

    @Override
    public Physician getById(Integer id) {
        return repo.findByEmployeeIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Physician not found with id: " + id)
                );
    }

    @Override
    public Page<Physician> getAllPaged(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

//    @Override
//    public List<Physician> getAllSorted() {
//        return repo.findAll(Sort.by("name"));
//    }
//
//    @Override
//    public boolean exists(Integer id) {
//        return repo.existsById(id);
//    }
//
//    @Override
//    public long count() {
//        return repo.count();
//    }
    
    @Override
    public List<Physician> getByPosition(String position) {
        return repo.findByPositionAndIsActiveTrue(position);
    }

    @Override
    public Physician save(Physician physician) {
        return repo.save(physician);
    }
    
    @Override
    public void delete(Integer id) {
        Physician physician = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Physician not found"));

        physician.setIsActive(false); // 👈 SOFT DELETE

        repo.save(physician);
    }
}