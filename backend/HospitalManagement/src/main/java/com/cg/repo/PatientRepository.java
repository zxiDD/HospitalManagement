package com.cg.repo;

import com.cg.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByNameAndIsActiveTrue(String name);

    List<Patient> findByNameAndAddressAndIsActiveTrue(String name, String address);
    
    List<Patient> findByIsActiveTrue();
    
    Optional<Patient> findByPhoneAndIsActiveTrue(String phone);
    
    List<Patient> findByPhysicianEmployeeId(Integer employeeId);
    
    Optional<Patient> findBySsnAndIsActiveTrue(Long ssn);
}