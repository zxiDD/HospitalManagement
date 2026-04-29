package com.cg.controller;

import com.cg.dto.NurseDTO;
import com.cg.entity.Nurse;
import com.cg.service.NurseService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    private final NurseService service;

    public NurseController(NurseService service) {
        this.service = service;
    }

    private NurseDTO mapToDTO(Nurse n) {
        return new NurseDTO(
                n.getEmployeeId(),
                n.getName(),
                n.getPosition(),
                n.getRegistered()
        );
    }

    @GetMapping
    public ResponseEntity<List<NurseDTO>> getAll() {
        List<NurseDTO> list = service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{employeeId}")
    public ResponseEntity<NurseDTO> getById(@PathVariable Integer employeeId) {
        Nurse nurse = service.getById(employeeId);
        return ResponseEntity.ok(mapToDTO(nurse));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<NurseDTO>> getAllSorted() {
        List<NurseDTO> list = service.getAllSorted()
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
    public ResponseEntity<NurseDTO> create(@Valid @RequestBody NurseDTO dto) {

        Nurse n = new Nurse();
        n.setEmployeeId(dto.getEmployeeId());
        n.setName(dto.getName());
        n.setPosition(dto.getPosition());
        n.setRegistered(dto.getRegistered());

        Nurse saved = service.save(n);

        return ResponseEntity.status(201).body(mapToDTO(saved));
    }
}