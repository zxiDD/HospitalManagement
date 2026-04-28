package com.cg.controller;

import com.cg.dto.NurseDTO;
import com.cg.entity.Nurse;
import com.cg.service.NurseService;
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
    public List<NurseDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{employeeId}")
    public NurseDTO getById(@PathVariable Integer employeeId) {
        return mapToDTO(service.getById(employeeId));
    }
    
    @GetMapping("/sorted")
    public List<NurseDTO> getAllSorted() {
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