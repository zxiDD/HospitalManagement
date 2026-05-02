package com.cg.service;

import com.cg.dto.MedicationDTO;
import com.cg.entity.Medication;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.MedicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    // 🔥 helper method
    private MedicationDTO mapToDTO(Medication m) {
        return new MedicationDTO(
                m.getCode(),
                m.getName(),
                m.getBrand(),
                m.getDescription()
        );
    }
    
    @Override
    public List<MedicationDTO> getAll() {
        List<Medication> meds = medicationRepository.findAll();
        List<MedicationDTO> dtoList = new ArrayList<>();

        for (Medication m : meds) {
            dtoList.add(mapToDTO(m));
        }

        return dtoList;
    }

    @Override
    public MedicationDTO getById(Integer code) {
    	if(code<=0) {
    		throw new BadRequestException("code can not be negative or 0");
    	}
        Medication m = medicationRepository.findById(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medication not found with code: " + code));

        return mapToDTO(m);
    }

    @Override
    public MedicationDTO getByName(String name) {
        Medication m = medicationRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medication not found with name: " + name));

        return mapToDTO(m);
    }

    @Override
    public List<MedicationDTO> getByBrand(String brand) {
        List<Medication> meds = medicationRepository.findByBrand(brand);
        List<MedicationDTO> dtoList = new ArrayList<>();

        for (Medication m : meds) {
            dtoList.add(mapToDTO(m));
        }

        return dtoList;
    }

    @Override
    public MedicationDTO getByNameAndBrand(String name, String brand) {
        Medication m = medicationRepository.findByNameAndBrand(name, brand)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medication not found with name: " + name + " and brand: " + brand));

        return mapToDTO(m);
    }

//    @Override
//    public List<MedicationDTO> getAllSorted() {
//        List<Medication> meds = medicationRepository.findAllByOrderByCodeAsc();
//        List<MedicationDTO> dtoList = new ArrayList<>();
//
//        for (Medication m : meds) {
//            dtoList.add(mapToDTO(m));
//        }
//
//        return dtoList;
//    }
//
//    @Override
//    public boolean exists(Integer code) {
//        return medicationRepository.existsById(code);
//    }
//
//    @Override
//    public long count() {
//        return medicationRepository.count();
//    }

    @Override
    public MedicationDTO create(MedicationDTO dto) {

        // 🔴 DUPLICATE CHECK (ADD HERE)
        if (medicationRepository
                .findByNameAndBrand(dto.getName(), dto.getBrand())
                .isPresent()) {

            throw new DuplicateResourceException(
                    "Medication already exists with name: " 
                    + dto.getName() + " and brand: " + dto.getBrand());
        }

        // 🔹 Create entity
        Medication med = new Medication();
        med.setName(dto.getName());
        med.setBrand(dto.getBrand());
        med.setDescription(dto.getDescription());

        // 🔹 Save
        Medication saved = medicationRepository.save(med);

        return mapToDTO(saved);
    }


}