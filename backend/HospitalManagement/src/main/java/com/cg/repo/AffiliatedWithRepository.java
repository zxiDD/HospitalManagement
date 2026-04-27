package com.cg.repo;

import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliatedWithRepository
        extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

    List<AffiliatedWith> findByPhysicianEmployeeId(Integer physicianId);

    List<AffiliatedWith> findByDepartmentDepartmentId(Integer departmentId);

    List<AffiliatedWith> findByPrimaryAffiliationTrue();

    // ✅ ADD THIS
    Optional<AffiliatedWith> findByPhysicianEmployeeIdAndPrimaryAffiliationTrue(Integer physicianId);
}