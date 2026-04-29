package com.cg.controller;

import com.cg.dto.MedicationDTO;
import com.cg.exception.ValidationException;
import com.cg.service.MedicationService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @GetMapping
    public ResponseEntity<List<MedicationDTO>> getAll() {
        return ResponseEntity.ok(medicationService.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<MedicationDTO> getById(@PathVariable Integer code) {
        return ResponseEntity.ok(medicationService.getById(code));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MedicationDTO> getByName(@PathVariable String name) {
        return ResponseEntity.ok(medicationService.getByName(name));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<MedicationDTO>> getByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(medicationService.getByBrand(brand));
    }

    @GetMapping("/search")
    public ResponseEntity<MedicationDTO> getByNameAndBrand(@RequestParam String name,
                                                          @RequestParam String brand) {
        return ResponseEntity.ok(medicationService.getByNameAndBrand(name, brand));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<MedicationDTO>> getAllSorted() {
        return ResponseEntity.ok(medicationService.getAllSorted());
    }

    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer code) {
        return ResponseEntity.ok(medicationService.exists(code));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(medicationService.count());
    }

    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(
            @Valid @RequestBody MedicationDTO dto,
            BindingResult br) {

        if (br.hasErrors()) {
            throw new ValidationException(br.getFieldErrors());
        }

        MedicationDTO created = medicationService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}