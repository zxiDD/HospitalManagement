package com.cg.service;

import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;

import java.util.List;

public interface AffiliatedWithService {

    List<AffiliatedWith> getAll();

    AffiliatedWith getById(AffiliatedWithId id);

    List<AffiliatedWith> getByPhysicianId(Integer physicianId);

    List<AffiliatedWith> getByDepartmentId(Integer departmentId);

    List<AffiliatedWith> getPrimaryAffiliations();

    Department getPrimaryDepartment(Integer physicianId);

    boolean exists(AffiliatedWithId id);

    long count();
}