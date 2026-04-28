package com.cg.controller;

import com.cg.entity.Physician;
import com.cg.service.PhysicianService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/physicians")
public class PhysicianController {

    private final PhysicianService service;

    public PhysicianController(PhysicianService service) {
        this.service = service;
    }

    @GetMapping
    public List<Physician> getAll() {
        return service.getAll();
    }

    @GetMapping("/{employeeId}")
    public Physician getById(@PathVariable Integer employeeId) {
        return service.getById(employeeId);
    }

    @GetMapping("/sorted")
    public List<Physician> getAllSorted() {
        return service.getAllSorted();
    }

    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    @GetMapping("/exists/{employeeId}")
    public boolean exists(@PathVariable Integer employeeId) {
        return service.exists(employeeId);
    }
}