package com.cg.controller;

import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.exception.ValidationException;
import com.cg.service.PhysicianService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "BearerAuth")
public class PhysicianController {

    private final PhysicianService service;

    public PhysicianController(PhysicianService service) {
        this.service = service;
    }

    private PhysicianDTO mapToDTO(Physician p) {
        return new PhysicianDTO(
                p.getEmployeeId(),
                p.getName(),
                p.getPosition(),
                p.getSsn()
        );
    }

    @GetMapping("/physicians")
    public ResponseEntity<List<PhysicianDTO>> getAll() {
        List<PhysicianDTO> list = service.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/physicians/id/{employeeId}")
    public ResponseEntity<PhysicianDTO> getById(@PathVariable Integer employeeId) {
        Physician physician = service.getById(employeeId);
        return ResponseEntity.ok(mapToDTO(physician));
    }
//
//    @GetMapping("/sorted")
//    public ResponseEntity<List<PhysicianDTO>> getAllSorted() {
//        List<PhysicianDTO> list = service.getAllSorted()
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(list);
//    }

//    @GetMapping("/count")
//    public ResponseEntity<Long> count() {
//        return ResponseEntity.ok(service.count());
//    }
//
//    @GetMapping("/exists/{employeeId}")
//    public ResponseEntity<Boolean> exists(@PathVariable Integer employeeId) {
//        return ResponseEntity.ok(service.exists(employeeId));
//    }

    @PostMapping("/admin/physicians")
    public ResponseEntity<PhysicianDTO> create(@Valid @RequestBody PhysicianDTO dto, BindingResult br) {
    	
    	if (br.hasErrors()) {
			throw new ValidationException(br.getFieldErrors());
		}

        Physician p = new Physician();
        p.setName(dto.getName());
        p.setPosition(dto.getPosition());
        p.setSsn(dto.getSsn());

        Physician saved = service.save(p);

        return ResponseEntity.status(201).body(mapToDTO(saved));
    }
    
    @PutMapping("admin/physicians/{employeeId}")
    public ResponseEntity<PhysicianDTO> update(
            @PathVariable Integer employeeId,
            @Valid @RequestBody PhysicianDTO dto) {

        Physician existing = service.getById(employeeId);

        existing.setName(dto.getName());
        existing.setPosition(dto.getPosition());
        existing.setSsn(dto.getSsn());

        Physician updated = service.save(existing);

        return ResponseEntity.ok(mapToDTO(updated));
    }
    
    //Get physicians my positions 
    @GetMapping("/physicians/position/{position}")
    public ResponseEntity<List<PhysicianDTO>> getByPosition(@PathVariable String position) {

        List<PhysicianDTO> list = service.getByPosition(position)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
    
    @DeleteMapping("admin/physicians/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer employeeId) {

        service.delete(employeeId); // now soft delete

        return ResponseEntity.noContent().build();
    }
}