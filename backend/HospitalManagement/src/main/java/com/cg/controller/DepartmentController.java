package com.cg.controller;

import com.cg.dto.DepartmentDTO;
import com.cg.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        List<DepartmentDTO> list = departmentService.getAll();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
        	return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Integer id) {
        DepartmentDTO dto = departmentService.getById(id);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
        	return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DepartmentDTO> getByName(@PathVariable String name) {
        DepartmentDTO dto = departmentService.getByName(name);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
        	return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/head/{headId}")
    public ResponseEntity<List<DepartmentDTO>> getByHeadId(@PathVariable Integer headId) {
        List<DepartmentDTO> list = departmentService.getByHeadId(headId);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
        	return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<DepartmentDTO>> getSorted() {
        List<DepartmentDTO> list = departmentService.getAllSorted();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
        	return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer id) {
        boolean exists = departmentService.exists(id);

        if (exists) {
            return ResponseEntity.ok(true);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = departmentService.count();

        if (count > 0) {
            return ResponseEntity.ok(count);
        } else {
            return new ResponseEntity<>(0L, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO dto) {
        DepartmentDTO created = departmentService.create(dto);

        if (created != null) {
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }
}