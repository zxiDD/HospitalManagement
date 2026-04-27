package com.cg.repo;

import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffiliatedWithRepository 
        extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

    // All affiliations of a physician
    List<AffiliatedWith> findByPhysician(Physician physician);

    // All physicians in a department
    List<AffiliatedWith> findByDepartment(Department department);

    // Only primary affiliations
    List<AffiliatedWith> findByPrimaryAffiliationTrue();

}
