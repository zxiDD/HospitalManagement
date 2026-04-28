package com.cg.service;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;

import java.util.List;

public interface AffiliatedWithService {

    List<AffiliatedWithDTO> getAll();

    AffiliatedWithDTO getById(AffiliatedWithId id);

    List<AffiliatedWithDTO> getByPhysicianId(Integer physicianId);

    List<AffiliatedWithDTO> getByDepartmentId(Integer departmentId);

    List<AffiliatedWithDTO> getPrimaryAffiliations();

    Department getPrimaryDepartment(Integer physicianId);

    boolean exists(AffiliatedWithId id);

    long count();
}