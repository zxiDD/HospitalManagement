package com.cg.service;

import com.cg.entity.Patient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {

    List<Patient> getAll();

    Patient getById(Long id);

    List<Patient> getByName(String name);

    List<Patient> getByNameAndAddress(String name, String address);

    Page<Patient> getAllPaged(int page, int size);

    List<Patient> getAllSorted();

    boolean exists(Long id);

    long count();
}