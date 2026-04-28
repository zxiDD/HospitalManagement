package com.cg.controller;

import com.cg.entity.Patient;
import com.cg.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    // GET all patients
    @GetMapping
    public List<Patient> getAll() {
        return service.getAll();
    }

    // GET patient by SSN
    @GetMapping("/{ssn}")
    public Patient getBySsn(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET by name
    @GetMapping("/name/{name}")
    public List<Patient> getByName(@PathVariable String name) {
        return service.getByName(name);
    }

    // GET by name and address
    @GetMapping("/search")
    public List<Patient> getByNameAndAddress(@RequestParam String name,
                                             @RequestParam String address) {
        return service.getByNameAndAddress(name, address);
    }

    // GET sorted
    @GetMapping("/sorted")
    public List<Patient> getAllSorted() {
        return service.getAllSorted();
    }

    // GET count
    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    // GET exists
    @GetMapping("/exists/{ssn}")
    public boolean exists(@PathVariable Long ssn) {
        return service.exists(ssn);
    }
}