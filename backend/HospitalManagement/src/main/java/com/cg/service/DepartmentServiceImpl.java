package com.cg.service;

import com.cg.dto.DepartmentDTO;
import com.cg.entity.Department;
import com.cg.entity.Physician;
import com.cg.repo.DepartmentRepository;
import com.cg.repo.PhysicianRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PhysicianRepository physicianRepository;

    @Override
    public List<DepartmentDTO> getAll() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> dtoList = new ArrayList<>();

        for (Department d : departments) {
            dtoList.add(new DepartmentDTO(
                    d.getDepartmentId(),
                    d.getName(),
                    d.getHead().getEmployeeId(),
                    d.getHead().getName()
            ));
        }
        return dtoList;
    }

    @Override
    public DepartmentDTO getById(Integer id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        return new DepartmentDTO(
                d.getDepartmentId(),
                d.getName(),
                d.getHead().getEmployeeId(),
                d.getHead().getName()
        );
    }

    @Override
    public DepartmentDTO getByName(String name) {
        Department d = departmentRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Department not found with name: " + name));

        return new DepartmentDTO(
                d.getDepartmentId(),
                d.getName(),
                d.getHead().getEmployeeId(),
                d.getHead().getName()
        );
    }

    @Override
    public List<DepartmentDTO> getByHeadId(Integer headId) {
        List<Department> departments = departmentRepository.findByHead_EmployeeId(headId);
        List<DepartmentDTO> dtoList = new ArrayList<>();

        for (Department d : departments) {
            dtoList.add(new DepartmentDTO(
                    d.getDepartmentId(),
                    d.getName(),
                    d.getHead().getEmployeeId(),
                    d.getHead().getName()
            ));
        }
        return dtoList;
    }

    @Override
    public List<DepartmentDTO> getAllSorted() {
        List<Department> departments = departmentRepository.findAllByOrderByDepartmentIdAsc();
        List<DepartmentDTO> dtoList = new ArrayList<>();

        for (Department d : departments) {
            dtoList.add(new DepartmentDTO(
                    d.getDepartmentId(),
                    d.getName(),
                    d.getHead().getEmployeeId(),
                    d.getHead().getName()
            ));
        }
        return dtoList;
    }

    @Override
    public boolean exists(Integer id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public long count() {
        return departmentRepository.count();
    }

    @Override
    public DepartmentDTO create(DepartmentDTO dto) {

        // 🔹 Fetch Physician (head)
        Physician head = physicianRepository.findById(dto.getHeadId())
                .orElseThrow(() -> new RuntimeException("Physician not found with id: " + dto.getHeadId()));

        // 🔹 Convert DTO → Entity
        Department dept = new Department();
        dept.setName(dto.getName());
        dept.setHead(head);

        // 🔹 Save
        Department saved = departmentRepository.save(dept);

        // 🔹 Convert Entity → DTO
        return new DepartmentDTO(
                saved.getDepartmentId(),
                saved.getName(),
                saved.getHead().getEmployeeId(),
                saved.getHead().getName()
        );
    }
}
