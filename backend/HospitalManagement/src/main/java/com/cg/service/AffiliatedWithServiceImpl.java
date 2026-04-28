package com.cg.service;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.repo.AffiliatedWithRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AffiliatedWithServiceImpl implements AffiliatedWithService {

    @Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Override
    public List<AffiliatedWithDTO> getAll() {
        List<AffiliatedWith> list = affiliatedWithRepository.findAll();
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(new AffiliatedWithDTO(
                    a.getPhysician().getEmployeeId(),
                    a.getPhysician().getName(),
                    a.getDepartment().getDepartmentId(),
                    a.getDepartment().getName(),
                    a.getPrimaryAffiliation()
            ));
        }

        return dtoList;
    }

    @Override
    public AffiliatedWithDTO getById(AffiliatedWithId id) {
        AffiliatedWith a = affiliatedWithRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affiliation not found"));

        return new AffiliatedWithDTO(
                a.getPhysician().getEmployeeId(),
                a.getPhysician().getName(),
                a.getDepartment().getDepartmentId(),
                a.getDepartment().getName(),
                a.getPrimaryAffiliation()
        );
    }

    @Override
    public List<AffiliatedWithDTO> getByPhysicianId(Integer physicianId) {
        List<AffiliatedWith> list = affiliatedWithRepository.findByPhysician_EmployeeId(physicianId);
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(new AffiliatedWithDTO(
                    a.getPhysician().getEmployeeId(),
                    a.getPhysician().getName(),
                    a.getDepartment().getDepartmentId(),
                    a.getDepartment().getName(),
                    a.getPrimaryAffiliation()
            ));
        }

        return dtoList;
    }

    @Override
    public List<AffiliatedWithDTO> getByDepartmentId(Integer departmentId) {
        List<AffiliatedWith> list = affiliatedWithRepository.findByDepartment_DepartmentId(departmentId);
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(new AffiliatedWithDTO(
                    a.getPhysician().getEmployeeId(),
                    a.getPhysician().getName(),
                    a.getDepartment().getDepartmentId(),
                    a.getDepartment().getName(),
                    a.getPrimaryAffiliation()
            ));
        }

        return dtoList;
    }

    @Override
    public List<AffiliatedWithDTO> getPrimaryAffiliations() {
        List<AffiliatedWith> list = affiliatedWithRepository.findByPrimaryAffiliationTrue();
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(new AffiliatedWithDTO(
                    a.getPhysician().getEmployeeId(),
                    a.getPhysician().getName(),
                    a.getDepartment().getDepartmentId(),
                    a.getDepartment().getName(),
                    a.getPrimaryAffiliation()
            ));
        }

        return dtoList;
    }

    @Override
    public Department getPrimaryDepartment(Integer physicianId) {
        return affiliatedWithRepository
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(physicianId)
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