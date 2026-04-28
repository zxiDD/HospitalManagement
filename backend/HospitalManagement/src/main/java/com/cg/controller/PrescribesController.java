package com.cg.controller;

import com.cg.dto.PrescribesDTO;
import com.cg.entity.Prescribes;
import com.cg.service.PrescribesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prescribes")
public class PrescribesController {

    private final PrescribesService service;

    public PrescribesController(PrescribesService service) {
        this.service = service;
    }

    private PrescribesDTO mapToDTO(Prescribes p) {
        return new PrescribesDTO(
                p.getPhysician().getEmployeeId(),
                p.getPatient().getSsn(),
                p.getMedication().getCode(),
                p.getId().getDate(),
                p.getDose(),
                p.getAppointment() != null ? p.getAppointment().getAppointmentID(): null
        );
    }

    @GetMapping
    public List<PrescribesDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/count")
    public long count() {
        return service.count();
    }
    
    @GetMapping("/sorted")
    public List<PrescribesDTO> getAllSorted() {
        return service.getAllSorted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/physician/{physicianId}")
    public List<PrescribesDTO> getByPhysician(@PathVariable Integer physicianId) {
        return service.getAll()
                .stream()
                .filter(p -> p.getPhysician().getEmployeeId().equals(physicianId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/patient/{ssn}")
    public List<PrescribesDTO> getByPatient(@PathVariable Long ssn) {
        return service.getAll()
                .stream()
                .filter(p -> p.getPatient().getSsn().equals(ssn))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}