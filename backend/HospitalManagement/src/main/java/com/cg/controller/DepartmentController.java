package com.cg.controller;

import com.cg.dto.DepartmentDTO;
import com.cg.exception.ValidationException;
import com.cg.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DepartmentDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(departmentService.getByName(name));
    }

    @GetMapping("/head/{headId}")
    public ResponseEntity<List<DepartmentDTO>> getByHeadId(@PathVariable Integer headId) {
        return ResponseEntity.ok(departmentService.getByHeadId(headId));
    }

//    @GetMapping("/sorted")
//    public ResponseEntity<List<DepartmentDTO>> getSorted() {
//        return ResponseEntity.ok(departmentService.getAllSorted());
//    }
//
//    @GetMapping("/exists/{id}")
//    public ResponseEntity<Boolean> exists(@PathVariable Integer id) {
//        return ResponseEntity.ok(departmentService.exists(id));
//    }
//
//    @GetMapping("/count")
//    public ResponseEntity<Long> count() {
//        return ResponseEntity.ok(departmentService.count());
//    }

    @PostMapping("/admin/departments")
    public ResponseEntity<DepartmentDTO> createDepartment(
            @Valid @RequestBody DepartmentDTO dto,
            BindingResult br) {

        if (br.hasErrors()) {
            throw new ValidationException(br.getFieldErrors());
        }

        DepartmentDTO created = departmentService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}