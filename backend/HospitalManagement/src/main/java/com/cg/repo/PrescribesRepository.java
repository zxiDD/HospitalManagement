package com.cg.repo;

import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescribesRepository extends JpaRepository<Prescribes, PrescribesId> {
}