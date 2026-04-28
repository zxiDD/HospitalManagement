package com.cg.controller;

import com.cg.dto.PatientDTO;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;
    private final PhysicianService physicianService;

    public PatientController(PatientService service, PhysicianService physicianService) {
        this.service = service;
        this.physicianService = physicianService;
    }

    private PatientDTO mapToDTO(Patient p) {
        return new PatientDTO(
                p.getSsn(),
                p.getName(),
                p.getAddress(),
                p.getPhone(),
                p.getInsuranceId(),
                p.getPhysician() != null ? p.getPhysician().getEmployeeId() : null
        );
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAll() {
        List<PatientDTO> list = service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{ssn}")
    public PatientDTO getBySsn(@PathVariable Long ssn) {
        return mapToDTO(service.getById(ssn));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<PatientDTO>> getByName(@PathVariable String name) {
        List<PatientDTO> list = service.getByName(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> getByNameAndAddress(
            @RequestParam String name,
            @RequestParam String address) {

        List<PatientDTO> list = service.getByNameAndAddress(name, address)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<PatientDTO>> getAllSorted() {
        List<PatientDTO> list = service.getAllSorted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @GetMapping("/exists/{ssn}")
    public ResponseEntity<Boolean> exists(@PathVariable Long ssn) {
        return ResponseEntity.ok(service.exists(ssn));
    }

    @PostMapping
    public ResponseEntity<PatientDTO> create(@RequestBody PatientDTO dto) {

        if (dto.getPhysicianId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Physician physician = physicianService.getById(dto.getPhysicianId());

        Patient patient = new Patient();
        patient.setSsn(dto.getSsn());
        patient.setName(dto.getName());
        patient.setAddress(dto.getAddress());
        patient.setPhone(dto.getPhone());
        patient.setInsuranceId(dto.getInsuranceId());
        patient.setPhysician(physician);

        Patient saved = service.save(patient);

        return ResponseEntity.ok(mapToDTO(saved));
    }
}