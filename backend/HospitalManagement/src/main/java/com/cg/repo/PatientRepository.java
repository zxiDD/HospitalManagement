package com.cg.repo;

import com.cg.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByName(String name);

    List<Patient> findByNameAndAddress(String name, String address);
    
    List<Patient> findByIsActiveTrue();
    
    Optional<Patient> findByPhone(String phone);
    
    List<Patient> findByPhysicianEmployeeId(Integer employeeId);
}