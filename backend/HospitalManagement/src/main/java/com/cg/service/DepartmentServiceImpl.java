package com.cg.service;

import com.cg.dto.DepartmentDTO;
import com.cg.entity.Department;
import com.cg.repo.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

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
        List<Department> departments = departmentRepository.findAllByOrderByNameAsc();
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
}
