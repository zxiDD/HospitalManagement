package com.cg.service;


import com.cg.entity.Medication;
import java.util.List;

public interface MedicationService {

    List<Medication> getAll();

    Medication getById(Integer code);

    Medication getByName(String name);

    List<Medication> getByBrand(String brand);

    Medication getByNameAndBrand(String name, String brand);

    List<Medication> getAllSorted();

    boolean exists(Integer code);

    long count();
}