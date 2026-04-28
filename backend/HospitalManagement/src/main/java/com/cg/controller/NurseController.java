package com.cg.controller;

import com.cg.entity.Nurse;
import com.cg.service.NurseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    private final NurseService service;

    public NurseController(NurseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Nurse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{employeeId}")
    public Nurse getById(@PathVariable Integer employeeId) {
        return service.getById(employeeId);
    }

    @GetMapping("/sorted")
    public List<Nurse> getAllSorted() {
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