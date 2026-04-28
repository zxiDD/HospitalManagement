package com.cg.controller;

import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.service.PhysicianService;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PhysicianDTO>> getAll() {
        List<PhysicianDTO> list = service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{employeeId}")
    public ResponseEntity<PhysicianDTO> getById(@PathVariable Integer employeeId) {
        Physician physician = service.getById(employeeId);
        return ResponseEntity.ok(mapToDTO(physician));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<PhysicianDTO>> getAllSorted() {
        List<PhysicianDTO> list = service.getAllSorted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @GetMapping("/exists/{employeeId}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(service.exists(employeeId));
    }

    @PostMapping
    public ResponseEntity<PhysicianDTO> create(@RequestBody PhysicianDTO dto) {

        if (dto.getEmployeeId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Physician p = new Physician();
        p.setEmployeeId(dto.getEmployeeId());
        p.setName(dto.getName());
        p.setPosition(dto.getPosition());

        Physician saved = service.save(p);

        return ResponseEntity.status(201).body(mapToDTO(saved));
    }
}