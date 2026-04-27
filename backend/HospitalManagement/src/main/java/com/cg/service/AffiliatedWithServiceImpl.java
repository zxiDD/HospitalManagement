package com.cg.service;

import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.repo.AffiliatedWithRepository;
import com.cg.service.AffiliatedWithService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AffiliatedWithServiceImpl implements AffiliatedWithService {
	
	@Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Override
    public List<AffiliatedWith> getAll() {
        return affiliatedWithRepository.findAll();
    }

    @Override
    public AffiliatedWith getById(AffiliatedWithId id) {
        return affiliatedWithRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affiliation not found"));
    }

    @Override
    public List<AffiliatedWith> getByPhysicianId(Integer physicianId) {
        return affiliatedWithRepository.findByPhysicianEmployeeId(physicianId);
    }

    @Override
    public List<AffiliatedWith> getByDepartmentId(Integer departmentId) {
        return affiliatedWithRepository.findByDepartmentDepartmentId(departmentId);
    }

    @Override
    public List<AffiliatedWith> getPrimaryAffiliations() {
        return affiliatedWithRepository.findByPrimaryAffiliationTrue();
    }

    @Override
    public Department getPrimaryDepartment(Integer physicianId) {
        return affiliatedWithRepository
                .findByPhysicianEmployeeIdAndPrimaryAffiliationTrue(physicianId)
                .orElseThrow(() -> new RuntimeException("Primary affiliation not found"))
                .getDepartment();
    }

    @Override
    public boolean exists(AffiliatedWithId id) {
        return affiliatedWithRepository.existsById(id);
    }

    @Override
    public long count() {
        return affiliatedWithRepository.count();
    }
}
