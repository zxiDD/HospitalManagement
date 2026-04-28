package com.cg.controller;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.service.AffiliatedWithService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/affiliations")
public class AffiliatedWithController {

    @Autowired
    private AffiliatedWithService affiliatedWithService;

    @PostMapping
    public ResponseEntity<AffiliatedWithDTO> createAffiliation(@RequestBody AffiliatedWithDTO dto) {
        AffiliatedWithDTO created = affiliatedWithService.create(dto);

        if (created != null) {
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AffiliatedWithDTO>> getAll() {
        List<AffiliatedWithDTO> list = affiliatedWithService.getAll();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/{physicianId}/{departmentId}")
    public ResponseEntity<AffiliatedWithDTO> getById(@PathVariable Integer physicianId,
                                                    @PathVariable Integer departmentId) {

        AffiliatedWithId id = new AffiliatedWithId();
        id.setPhysicianId(physicianId);
        id.setDepartmentId(departmentId);

        AffiliatedWithDTO dto = affiliatedWithService.getById(id);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/physician/{physicianId}")
    public ResponseEntity<List<AffiliatedWithDTO>> getByPhysicianId(@PathVariable Integer physicianId) {
        List<AffiliatedWithDTO> list = affiliatedWithService.getByPhysicianId(physicianId);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<AffiliatedWithDTO>> getByDepartmentId(@PathVariable Integer departmentId) {
        List<AffiliatedWithDTO> list = affiliatedWithService.getByDepartmentId(departmentId);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/primary")
    public ResponseEntity<List<AffiliatedWithDTO>> getPrimaryAffiliations() {
        List<AffiliatedWithDTO> list = affiliatedWithService.getPrimaryAffiliations();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/primary/{physicianId}")
    public ResponseEntity<Department> getPrimaryDepartment(@PathVariable Integer physicianId) {
        Department dept = affiliatedWithService.getPrimaryDepartment(physicianId);

        if (dept != null) {
            return ResponseEntity.ok(dept);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/exists/{physicianId}/{departmentId}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer physicianId,
                                         @PathVariable Integer departmentId) {

        AffiliatedWithId id = new AffiliatedWithId();
        id.setPhysicianId(physicianId);
        id.setDepartmentId(departmentId);

        boolean exists = affiliatedWithService.exists(id);

        if (exists) {
            return ResponseEntity.ok(true);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = affiliatedWithService.count();

        if (count > 0) {
            return ResponseEntity.ok(count);
        } else {
            return new ResponseEntity<>(0L, HttpStatus.NO_CONTENT);
        }
    }
}