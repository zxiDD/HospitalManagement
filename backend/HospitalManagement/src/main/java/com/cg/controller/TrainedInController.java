package com.cg.controller;

import com.cg.dto.TrainedInDTO;
import com.cg.entity.*;
import com.cg.service.TrainedInService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainedin")
public class TrainedInController {

    @Autowired
    private TrainedInService service;

    private TrainedInDTO convertToDTO(TrainedIn t) {
        return new TrainedInDTO(
                t.getId().getPhysician(),
                t.getId().getTreatment(),
                t.getCertificationDate().toLocalDate(),
                t.getCertificationExpires().toLocalDate()
        );
    }

    @GetMapping
    public ResponseEntity<List<TrainedInDTO>> getAll() {
        List<TrainedInDTO> list = service.getAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/physician/{id}")
    public ResponseEntity<List<TrainedInDTO>> getByPhysician(@PathVariable Integer id) {
        List<TrainedInDTO> list = service.getByPhysicianId(id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }


    @PostMapping("/trainings")
    public ResponseEntity<?> addTraining(@Valid @RequestBody TrainedInDTO dto) {

        if (dto.getCertificationExpires().isBefore(dto.getCertificationDate())) {
            return ResponseEntity.badRequest().body("Expiry must be after certification date");
        }

        TrainedIn t = new TrainedIn();

        TrainedInId id = new TrainedInId(
                dto.getPhysicianId(),
                dto.getTreatmentId()
        );

        t.setId(id);

        Physician p = new Physician();
        p.setEmployeeId(dto.getPhysicianId());
        t.setPhysician(p);

        Procedures proc = new Procedures();
        proc.setCode(dto.getTreatmentId());
        t.setTreatment(proc);

        t.setCertificationDate(dto.getCertificationDate().atStartOfDay());
        t.setCertificationExpires(dto.getCertificationExpires().atStartOfDay());

        return ResponseEntity.status(201).body(service.save(t));
    }
}