package com.cg.service;

import com.cg.entity.Patient;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.BadRequestException;
import com.cg.repo.PatientRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repo;

    public PatientServiceImpl(PatientRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Patient> getAll() {
        return repo.findAll();
    }

    @Override
    public Patient getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found with SSN: " + id)
                );
    }

    @Override
    public List<Patient> getByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Patient> getByNameAndAddress(String name, String address) {
        return repo.findByNameAndAddress(name, address);
    }

    @Override
    public List<Patient> getAllSorted() {
        return repo.findAll(Sort.by("name"));
    }

    @Override
    public boolean exists(Long id) {
        return repo.existsById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public Patient save(Patient patient) {

        if (repo.existsById(patient.getSsn())) {
            throw new BadRequestException(
                    "Patient already exists with SSN: " + patient.getSsn()
            );
        }

        return repo.save(patient);
    }
}