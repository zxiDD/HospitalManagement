package com.cg.controller;

import com.cg.dto.DepartmentDTO;
import com.cg.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public List<DepartmentDTO> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public DepartmentDTO getById(@PathVariable Integer id) {
        return departmentService.getById(id);
    }

    @GetMapping("/name/{name}")
    public DepartmentDTO getByName(@PathVariable String name) {
        return departmentService.getByName(name);
    }

    @GetMapping("/head/{headId}")
    public List<DepartmentDTO> getByHeadId(@PathVariable Integer headId) {
        return departmentService.getByHeadId(headId);
    }

    @GetMapping("/sorted")
    public List<DepartmentDTO> getSorted() {
        return departmentService.getAllSorted();
    }

    @GetMapping("/exists/{id}")
    public boolean exists(@PathVariable Integer id) {
        return departmentService.exists(id);
    }

    @GetMapping("/count")
    public long count() {
        return departmentService.count();
    }
    
    @PostMapping
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO dto) {
        return departmentService.create(dto);
    }
}