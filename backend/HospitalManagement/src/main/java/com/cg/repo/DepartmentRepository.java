package com.cg.repo;


import com.cg.entity.Department;
import com.cg.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // Find department by name
    Department findByName(String name);

    // Find all departments headed by a specific physician
    List<Department> findByHead(Physician head);

}