package com.cg.service;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.entity.AffiliatedWith;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.entity.Physician;
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
    
    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public AffiliatedWithDTO create(AffiliatedWithDTO dto) {

        // 🔴 VALIDATION (put it here)
        if (dto.getPrimaryAffiliation()) {
            Optional<AffiliatedWith> existing =
                    affiliatedWithRepository
                    .findByPhysician_EmployeeIdAndPrimaryAffiliationTrue(dto.getPhysicianId());

            if (existing.isPresent()) {
                throw new RuntimeException("Primary affiliation already exists for this physician");
            }
        }

        // 🔹 Fetch Physician
        Physician physician = physicianRepository.findById(dto.getPhysicianId())
                .orElseThrow(() -> new RuntimeException("Physician not found"));

        // 🔹 Fetch Department
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // 🔹 Create ID
        AffiliatedWithId id = new AffiliatedWithId();
        id.setPhysicianId(dto.getPhysicianId());
        id.setDepartmentId(dto.getDepartmentId());

        // 🔹 Create Entity
        AffiliatedWith aff = new AffiliatedWith();
        aff.setId(id);
        aff.setPhysician(physician);
        aff.setDepartment(department);
        aff.setPrimaryAffiliation(dto.getPrimaryAffiliation());

        // 🔹 Save
        AffiliatedWith saved = affiliatedWithRepository.save(aff);

        // 🔹 Return DTO
        return new AffiliatedWithDTO(
                saved.getPhysician().getEmployeeId(),
                saved.getPhysician().getName(),
                saved.getDepartment().getDepartmentId(),
                saved.getDepartment().getName(),
                saved.getPrimaryAffiliation()
        );
    }
}