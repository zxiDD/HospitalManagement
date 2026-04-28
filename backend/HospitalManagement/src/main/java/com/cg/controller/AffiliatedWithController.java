package com.cg.controller;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.service.AffiliatedWithService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/affiliations")
public class AffiliatedWithController {

    @Autowired
    private AffiliatedWithService affiliatedWithService;

    @PostMapping
    public AffiliatedWithDTO createAffiliation(@RequestBody AffiliatedWithDTO dto) {
        return affiliatedWithService.create(dto);
    }
    
    @GetMapping
    public List<AffiliatedWithDTO> getAll() {
        return affiliatedWithService.getAll();
    }

    @GetMapping("/{physicianId}/{departmentId}")
    public AffiliatedWithDTO getById(@PathVariable Integer physicianId,
                                    @PathVariable Integer departmentId) {

        AffiliatedWithId id = new AffiliatedWithId();
        id.setPhysicianId(physicianId);
        id.setDepartmentId(departmentId);

        return affiliatedWithService.getById(id);
    }

    @GetMapping("/physician/{physicianId}")
    public List<AffiliatedWithDTO> getByPhysicianId(@PathVariable Integer physicianId) {
        return affiliatedWithService.getByPhysicianId(physicianId);
    }

    @GetMapping("/department/{departmentId}")
    public List<AffiliatedWithDTO> getByDepartmentId(@PathVariable Integer departmentId) {
        return affiliatedWithService.getByDepartmentId(departmentId);
    }

    @GetMapping("/primary")
    public List<AffiliatedWithDTO> getPrimaryAffiliations() {
        return affiliatedWithService.getPrimaryAffiliations();
    }

    @GetMapping("/primary/{physicianId}")
    public Department getPrimaryDepartment(@PathVariable Integer physicianId) {
        return affiliatedWithService.getPrimaryDepartment(physicianId);
    }

    @GetMapping("/exists/{physicianId}/{departmentId}")
    public boolean exists(@PathVariable Integer physicianId,
                          @PathVariable Integer departmentId) {

        AffiliatedWithId id = new AffiliatedWithId();
        id.setPhysicianId(physicianId);
        id.setDepartmentId(departmentId);

        return affiliatedWithService.exists(id);
    }

    @GetMapping("/count")
    public long count() {
        return affiliatedWithService.count();
    }
}