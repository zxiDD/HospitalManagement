package com.cg.service;

import com.cg.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    List<Patient> getAll();

    Patient getById(Long id);

    List<Patient> getByName(String name);

    List<Patient> getByNameAndAddress(String name, String address);

//    List<Patient> getAllSorted();
    
    Patient save(Patient patient);

//    boolean exists(Long id);
//
//    long count();
    
    void delete(Long ssn);
    
    Optional<Patient> getByPhone(String phone);
}