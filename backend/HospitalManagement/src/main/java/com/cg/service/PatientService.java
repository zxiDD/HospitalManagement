package com.cg.service;

import com.cg.entity.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getAll();

    Patient getById(Long id);

    List<Patient> getByName(String name);

    List<Patient> getByNameAndAddress(String name, String address);

    List<Patient> getAllSorted();

    boolean exists(Long id);

    long count();
}