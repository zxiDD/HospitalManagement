package com.cg.service;

import com.cg.dto.MedicationDTO;
import java.util.List;

public interface MedicationService {

    List<MedicationDTO> getAll();

    MedicationDTO getById(Integer code);

    MedicationDTO getByName(String name);

    List<MedicationDTO> getByBrand(String brand);

    MedicationDTO getByNameAndBrand(String name, String brand);

    List<MedicationDTO> getAllSorted();

    boolean exists(Integer code);

    long count();
}