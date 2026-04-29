package com.cg.repo;

import com.cg.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByName(String name);

    List<Patient> findByNameAndAddress(String name, String address);
    
    List<Patient> findByIsActiveTrue();
    
    List<Patient> findByPhone(String phone);
}