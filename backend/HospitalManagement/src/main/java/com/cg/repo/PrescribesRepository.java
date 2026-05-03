package com.cg.repo;

import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescribesRepository extends JpaRepository<Prescribes, PrescribesId> {
	List<Prescribes> findByIdPatient(Long patient);
}