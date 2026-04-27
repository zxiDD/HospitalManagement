package com.cg.service;

import com.cg.entity.Patient;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PatientRepository;
import com.cg.service.PatientService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repo;

    public PatientServiceImpl(PatientRepository repo) {
        this.repo = repo;
    }

    public List<Patient> getAll() {
        return repo.findAll();
    }

    public Patient getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    public List<Patient> getByName(String name) {
        return repo.findByName(name);
    }

    public List<Patient> getByNameAndAddress(String name, String address) {
        return repo.findByNameAndAddress(name, address);
    }

    public Page<Patient> getAllPaged(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    public List<Patient> getAllSorted() {
        return repo.findAll(Sort.by("name"));
    }

    public boolean exists(Long id) {
        return repo.existsById(id);
    }

    public long count() {
        return repo.count();
    }
}