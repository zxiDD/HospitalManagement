package com.cg.service;

import java.util.List;
import com.cg.dto.DepartmentDTO;

public interface DepartmentService {

    List<DepartmentDTO> getAll();

    DepartmentDTO getById(Integer id);

    DepartmentDTO getByName(String name);

    List<DepartmentDTO> getByHeadId(Integer headId);

    List<DepartmentDTO> getAllSorted();

    boolean exists(Integer id);

    long count();
}