package com.cg.controller;

import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.service.PhysicianService;

import jakarta.validation.Valid;
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
                p.getPosition(),
                p.getSsn()
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
//
//    @GetMapping("/sorted")
//    public ResponseEntity<List<PhysicianDTO>> getAllSorted() {
//        List<PhysicianDTO> list = service.getAllSorted()
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(list);
//    }

//    @GetMapping("/count")
//    public ResponseEntity<Long> count() {
//        return ResponseEntity.ok(service.count());
//    }
//
//    @GetMapping("/exists/{employeeId}")
//    public ResponseEntity<Boolean> exists(@PathVariable Integer employeeId) {
//        return ResponseEntity.ok(service.exists(employeeId));
//    }

    @PostMapping
    public ResponseEntity<PhysicianDTO> create(@Valid @RequestBody PhysicianDTO dto) {

        Physician p = new Physician();
        p.setEmployeeId(dto.getEmployeeId());
        p.setName(dto.getName());
        p.setPosition(dto.getPosition());
        p.setSsn(dto.getSsn());

        Physician saved = service.save(p);

        return ResponseEntity.status(201).body(mapToDTO(saved));
    }
    
    @PutMapping("/{employeeId}")
    public ResponseEntity<PhysicianDTO> update(
            @PathVariable Integer employeeId,
            @Valid @RequestBody PhysicianDTO dto) {

        Physician existing = service.getById(employeeId);

        existing.setName(dto.getName());
        existing.setPosition(dto.getPosition());
        existing.setSsn(dto.getSsn());

        Physician updated = service.save(existing);

        return ResponseEntity.ok(mapToDTO(updated));
    }
    
    //Get physicians my positions 
    @GetMapping("/position/{position}")
    public ResponseEntity<List<PhysicianDTO>> getByPosition(@PathVariable String position) {

        List<PhysicianDTO> list = service.getByPosition(position)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
    
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer employeeId) {

        service.delete(employeeId); // now soft delete

        return ResponseEntity.noContent().build();
    }
}