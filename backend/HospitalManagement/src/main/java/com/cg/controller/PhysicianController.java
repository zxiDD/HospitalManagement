package com.cg.controller;

import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.service.PhysicianService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/physicians")
public class PhysicianController {

    private final PhysicianService service;

    public PhysicianController(PhysicianService service) {
        this.service = service;
    }

    private PhysicianDTO mapToDTO(Physician p) {
        return new PhysicianDTO(
                p.getEmployeeId(),
                p.getName(),
                p.getPosition()
        );
    }

    @GetMapping
    public List<PhysicianDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{employeeId}")
    public PhysicianDTO getById(@PathVariable Integer employeeId) {
        return mapToDTO(service.getById(employeeId));
    }
    
    @GetMapping("/sorted")
    public List<PhysicianDTO> getAllSorted() {
        return service.getAllSorted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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