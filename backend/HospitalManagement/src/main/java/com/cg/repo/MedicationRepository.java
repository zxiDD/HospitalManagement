package com.cg.repo;

import com.cg.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    // Find by name (assuming unique or mostly unique)
    Optional<Medication> findByName(String name);

    // Find by brand
    List<Medication> findByBrand(String brand);

    // Find by name and brand (more precise search)
    Optional<Medication> findByNameAndBrand(String name, String brand);

    // Sorting
    List<Medication> findAllByOrderByCodeAsc();
}