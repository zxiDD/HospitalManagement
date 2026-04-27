package com.cg.repo;

import com.cg.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    List<Medication> findByBrand(String brand);
    Medication findByName(String name);

}