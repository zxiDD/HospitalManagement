package com.cg.controller;

import com.cg.dto.MedicationDTO;
import com.cg.service.MedicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @GetMapping
    public List<MedicationDTO> getAll() {
        return medicationService.getAll();
    }

    @GetMapping("/{code}")
    public MedicationDTO getById(@PathVariable Integer code) {
        return medicationService.getById(code);
    }

    @GetMapping("/name/{name}")
    public MedicationDTO getByName(@PathVariable String name) {
        return medicationService.getByName(name);
    }

    @GetMapping("/brand/{brand}")
    public List<MedicationDTO> getByBrand(@PathVariable String brand) {
        return medicationService.getByBrand(brand);
    }

    @GetMapping("/search")
    public MedicationDTO getByNameAndBrand(@RequestParam String name,
                                           @RequestParam String brand) {
        return medicationService.getByNameAndBrand(name, brand);
    }

    @GetMapping("/sorted")
    public List<MedicationDTO> getAllSorted() {
        return medicationService.getAllSorted();
    }

    @GetMapping("/exists/{code}")
    public boolean exists(@PathVariable Integer code) {
        return medicationService.exists(code);
    }

    @GetMapping("/count")
    public long count() {
        return medicationService.count();
    }
}