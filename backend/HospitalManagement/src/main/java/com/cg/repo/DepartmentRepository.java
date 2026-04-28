package com.cg.repo;


import com.cg.entity.Department;
import com.cg.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // Find department by name
    Optional<Department> findByName(String name);

        List<Department> findByHead_EmployeeId(Integer headId);

        // sorting
        List<Department> findAllByOrderByNameAsc();

    }

