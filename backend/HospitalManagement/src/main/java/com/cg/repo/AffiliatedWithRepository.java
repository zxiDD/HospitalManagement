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

    List<AffiliatedWith> findByPhysicianEmployeeID(Integer physicianId);

    List<AffiliatedWith> findByDepartmentDepartmentID(Integer departmentId);

    List<AffiliatedWith> findByPrimaryAffiliationTrue();

    // ✅ ADD THIS
    Optional<AffiliatedWith> findByPhysicianEmployeeIDAndPrimaryAffiliationTrue(Integer physicianId);
}