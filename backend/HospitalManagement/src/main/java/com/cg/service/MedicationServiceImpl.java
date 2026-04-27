package com.cg.service;

import com.cg.entity.Medication;
import com.cg.repo.MedicationRepository;
import com.cg.service.MedicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {
	@Autowired
    private MedicationRepository medicationRepository;

    @Override
    public List<Medication> getAll() {
        return medicationRepository.findAll();
    }

    @Override
    public Medication getById(Integer code) {
        return medicationRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Medication not found with code: " + code));
    }

    @Override
    public Medication getByName(String name) {
        return medicationRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Medication not found with name: " + name));
    }

    @Override
    public List<Medication> getByBrand(String brand) {
        return medicationRepository.findByBrand(brand);
    }

    @Override
    public Medication getByNameAndBrand(String name, String brand) {
        return medicationRepository.findByNameAndBrand(name, brand)
                .orElseThrow(() -> new RuntimeException(
                        "Medication not found with name: " + name + " and brand: " + brand));
    }

    @Override
    public List<Medication> getAllSorted() {
        return medicationRepository.findAllByOrderByNameAsc();
    }

    @Override
    public boolean exists(Integer code) {
        return medicationRepository.existsById(code);
    }

    @Override
    public long count() {
        return medicationRepository.count();
    }
}