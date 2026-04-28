package com.cg.service;

import com.cg.dto.MedicationDTO;
import com.cg.entity.Medication;
import com.cg.repo.MedicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    public List<MedicationDTO> getAll() {
        List<Medication> meds = medicationRepository.findAll();
        List<MedicationDTO> dtoList = new ArrayList<>();

        for (Medication m : meds) {
            dtoList.add(new MedicationDTO(
                    m.getCode(),
                    m.getName(),
                    m.getBrand(),
                    m.getDescription()
            ));
        }

        return dtoList;
    }

    @Override
    public MedicationDTO getById(Integer code) {
        Medication m = medicationRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Medication not found with code: " + code));

        return new MedicationDTO(
                m.getCode(),
                m.getName(),
                m.getBrand(),
                m.getDescription()
        );
    }

    @Override
    public MedicationDTO getByName(String name) {
        Medication m = medicationRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Medication not found with name: " + name));

        return new MedicationDTO(
                m.getCode(),
                m.getName(),
                m.getBrand(),
                m.getDescription()
        );
    }

    @Override
    public List<MedicationDTO> getByBrand(String brand) {
        List<Medication> meds = medicationRepository.findByBrand(brand);
        List<MedicationDTO> dtoList = new ArrayList<>();

        for (Medication m : meds) {
            dtoList.add(new MedicationDTO(
                    m.getCode(),
                    m.getName(),
                    m.getBrand(),
                    m.getDescription()
            ));
        }

        return dtoList;
    }

    @Override
    public MedicationDTO getByNameAndBrand(String name, String brand) {
        Medication m = medicationRepository.findByNameAndBrand(name, brand)
                .orElseThrow(() -> new RuntimeException(
                        "Medication not found with name: " + name + " and brand: " + brand));

        return new MedicationDTO(
                m.getCode(),
                m.getName(),
                m.getBrand(),
                m.getDescription()
        );
    }

    @Override
    public List<MedicationDTO> getAllSorted() {
        List<Medication> meds = medicationRepository.findAllByOrderByCodeAsc();
        List<MedicationDTO> dtoList = new ArrayList<>();

        for (Medication m : meds) {
            dtoList.add(new MedicationDTO(
                    m.getCode(),
                    m.getName(),
                    m.getBrand(),
                    m.getDescription()
            ));
        }

        return dtoList;
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