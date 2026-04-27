package com.cg.service;

import com.cg.entity.Department;
import com.cg.repo.DepartmentRepository;
import com.cg.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
    private DepartmentRepository departmentRepository;

    
    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Override
    public Department getByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Department not found with name: " + name));
    }

    @Override
    public List<Department> getByHeadId(Integer headId) {
        return departmentRepository.findByHeadEmployeeId(headId);
    }

    @Override
    public List<Department> getAllSorted() {
        return departmentRepository.findAllByOrderByNameAsc();
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
