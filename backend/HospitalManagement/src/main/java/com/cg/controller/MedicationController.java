package com.cg.controller;

import com.cg.dto.MedicationDTO;
import com.cg.service.MedicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @GetMapping
    public ResponseEntity<List<MedicationDTO>> getAll() {
        List<MedicationDTO> list = medicationService.getAll();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<MedicationDTO> getById(@PathVariable Integer code) {
        MedicationDTO dto = medicationService.getById(code);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MedicationDTO> getByName(@PathVariable String name) {
        MedicationDTO dto = medicationService.getByName(name);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<MedicationDTO>> getByBrand(@PathVariable String brand) {
        List<MedicationDTO> list = medicationService.getByBrand(brand);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<MedicationDTO> getByNameAndBrand(@RequestParam String name,
                                                           @RequestParam String brand) {
        MedicationDTO dto = medicationService.getByNameAndBrand(name, brand);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<MedicationDTO>> getAllSorted() {
        List<MedicationDTO> list = medicationService.getAllSorted();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer code) {
        boolean exists = medicationService.exists(code);

        if (exists) {
            return ResponseEntity.ok(true);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = medicationService.count();

        if (count > 0) {
            return ResponseEntity.ok(count);
        } else {
            return new ResponseEntity<>(0L, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(@RequestBody MedicationDTO dto) {
        MedicationDTO created = medicationService.create(dto);

        if (created != null) {
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }
}