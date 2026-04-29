package com.cg.service;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.dto.DepartmentDTO;
import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.entity.Physician;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AffiliatedWithRepository;
import com.cg.repo.DepartmentRepository;
import com.cg.repo.PhysicianRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AffiliatedWithServiceImpl implements AffiliatedWithService {

    @Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // 🔥 helper method (centralized mapping)
    private AffiliatedWithDTO mapToDTO(AffiliatedWith a) {
        return new AffiliatedWithDTO(
                a.getPhysician().getEmployeeId(),
                a.getPhysician().getName(),
                a.getDepartment().getDepartmentId(),
                a.getDepartment().getName(),
                a.getPrimaryAffiliation()
        );
    }
    
    @Override
    public List<AffiliatedWithDTO> getAll() {
        List<AffiliatedWith> list = affiliatedWithRepository.findAll();
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(mapToDTO(a));
        }

        return dtoList;
    }

    @Override
    public AffiliatedWithDTO getById(AffiliatedWithId id) {
        AffiliatedWith a = affiliatedWithRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliation not found"));

        return mapToDTO(a);
    }

    @Override
    public List<AffiliatedWithDTO> getByPhysicianId(Integer physicianId) {
        List<AffiliatedWith> list = affiliatedWithRepository.findByPhysician_EmployeeId(physicianId);
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(mapToDTO(a));
        }

        return dtoList;
    }

    @Override
    public List<AffiliatedWithDTO> getByDepartmentId(Integer departmentId) {
        List<AffiliatedWith> list = affiliatedWithRepository.findByDepartment_DepartmentId(departmentId);
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(mapToDTO(a));
        }

        return dtoList;
    }

    @Override
    public List<AffiliatedWithDTO> getPrimaryAffiliations() {
        List<AffiliatedWith> list = affiliatedWithRepository.findByPrimaryAffiliationTrue();
        List<AffiliatedWithDTO> dtoList = new ArrayList<>();

        for (AffiliatedWith a : list) {
            dtoList.add(mapToDTO(a));
        }

        return dtoList;
    }

    @Override
    public DepartmentDTO getPrimaryDepartment(Integer physicianId) {

        AffiliatedWith a = affiliatedWithRepository
                .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(physicianId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Primary affiliation not found"));

        Department d = a.getDepartment();

        return new DepartmentDTO(
                d.getDepartmentId(),
                d.getName(),
                d.getHead().getEmployeeId(),
                d.getHead().getName()
        );
    }

//    @Override
//    public boolean exists(AffiliatedWithId id) {
//        return affiliatedWithRepository.existsById(id);
//    }
//
//    @Override
//    public long count() {
//        return affiliatedWithRepository.count();
//    }

    @Override
    public AffiliatedWithDTO create(AffiliatedWithDTO dto) {

        // 🔴 Business rule: only one primary affiliation
        if (dto.getPrimaryAffiliation()) {
            Optional<AffiliatedWith> existing =
                    affiliatedWithRepository
                    .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(dto.getPhysicianId());

            if (existing.isPresent()) {
                throw new DuplicateResourceException(
                        "Primary affiliation already exists for this physician");
            }
        }

        Physician physician = physicianRepository.findById(dto.getPhysicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Physician not found"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        AffiliatedWithId id = new AffiliatedWithId();
        id.setPhysicianId(dto.getPhysicianId());
        id.setDepartmentId(dto.getDepartmentId());

        AffiliatedWith aff = new AffiliatedWith();
        aff.setId(id);
        aff.setPhysician(physician);
        aff.setDepartment(department);
        aff.setPrimaryAffiliation(dto.getPrimaryAffiliation());

        AffiliatedWith saved = affiliatedWithRepository.save(aff);

        return mapToDTO(saved);
    }

}