package com.cg.controller;

import com.cg.dto.PatientDTO;
import com.cg.entity.Patient;
import com.cg.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    private PatientDTO mapToDTO(Patient p) {
        return new PatientDTO(
                p.getSsn(),
                p.getName(),
                p.getAddress(),
                p.getPhone(),
                p.getInsuranceId(),
                p.getPhysician().getEmployeeId()
        );
    }

    @GetMapping
    public List<PatientDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{ssn}")
    public PatientDTO getBySsn(@PathVariable Long id) {
        return mapToDTO(service.getById(id));
    }
    
    @GetMapping("/name/{name}")
    public List<PatientDTO> getByName(@PathVariable String name) {
        return service.getByName(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<PatientDTO> getByNameAndAddress(@RequestParam String name,
                                                 @RequestParam String address) {
        return service.getByNameAndAddress(name, address)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/sorted")
    public List<PatientDTO> getAllSorted() {
        return service.getAllSorted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    @GetMapping("/exists/{ssn}")
    public boolean exists(@PathVariable Long ssn) {
        return service.exists(ssn);
    }
}