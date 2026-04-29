package com.cg.controller;

import com.cg.dto.NurseDTO;
import com.cg.dto.OnCallDTO;
import com.cg.entity.Nurse;
import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;
import com.cg.service.NurseService;
import com.cg.service.OnCallService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/nurses")
public class NurseController {

    private final NurseService service;
    private final OnCallService onCallService; 

    public NurseController(NurseService service, OnCallService onCallService) {
        this.service = service;
        this.onCallService = onCallService;
    }

    private NurseDTO mapToDTO(Nurse n) {
        return new NurseDTO(
                n.getEmployeeId(),
                n.getName(),
                n.getPosition(),
                n.getRegistered(),
                n.getSsn()
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

//    @GetMapping("/sorted")
//    public ResponseEntity<List<NurseDTO>> getAllSorted() {
//        List<NurseDTO> list = service.getAllSorted()
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(list);
//    }
//
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
    public ResponseEntity<NurseDTO> create(@Valid @RequestBody NurseDTO dto) {

        Nurse n = new Nurse();
        n.setEmployeeId(dto.getEmployeeId());
        n.setName(dto.getName());
        n.setPosition(dto.getPosition());
        n.setRegistered(dto.getRegistered());
        n.setSsn(dto.getSsn());

        Nurse saved = service.save(n);

        return ResponseEntity.status(201).body(mapToDTO(saved));
    }
    
    @PutMapping("/{employeeId}")
    public ResponseEntity<NurseDTO> update(
            @PathVariable Integer employeeId,
            @Valid @RequestBody NurseDTO dto) {

        Nurse existing = service.getById(employeeId);

        existing.setName(dto.getName());
        existing.setPosition(dto.getPosition());
        existing.setRegistered(dto.getRegistered());
        existing.setSsn(dto.getSsn());

        Nurse updated = service.save(existing);

        return ResponseEntity.ok(mapToDTO(updated));
    }
    
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer employeeId) {

        service.delete(employeeId); // soft delete

        return ResponseEntity.noContent().build();
    }
   
}