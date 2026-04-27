package com.cg.service;

import java.util.List;

import com.cg.entity.Department;

public interface DepartmentService {

    List<Department> getAll();

    Department getById(Integer id);

    Department getByName(String name);

    List<Department> getByHeadId(Integer headId);

    List<Department> getAllSorted();

    boolean exists(Integer id);

    long count();
}