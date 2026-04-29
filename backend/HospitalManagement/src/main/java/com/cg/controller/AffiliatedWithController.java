package com.cg.controller;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.dto.DepartmentDTO;
import com.cg.entity.AffiliatedWithId;
import com.cg.entity.Department;
import com.cg.exception.ValidationException;
import com.cg.service.AffiliatedWithService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AffiliatedWithController {

	@Autowired
	private AffiliatedWithService affiliatedWithService;

	@PostMapping
	public ResponseEntity<AffiliatedWithDTO> createAffiliation(@Valid @RequestBody AffiliatedWithDTO dto,
			BindingResult br) {

		if (br.hasErrors()) {
			throw new ValidationException(br.getFieldErrors());
		}

		AffiliatedWithDTO created = affiliatedWithService.create(dto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<AffiliatedWithDTO>> getAll() {
		return ResponseEntity.ok(affiliatedWithService.getAll());
	}

	@GetMapping("/{physicianId}/{departmentId}")
	public ResponseEntity<AffiliatedWithDTO> getById(@PathVariable Integer physicianId,
			@PathVariable Integer departmentId) {

		AffiliatedWithId id = new AffiliatedWithId();
		id.setPhysicianId(physicianId);
		id.setDepartmentId(departmentId);

		return ResponseEntity.ok(affiliatedWithService.getById(id));
	}

	@GetMapping("/physician/{physicianId}")
	public ResponseEntity<List<AffiliatedWithDTO>> getByPhysicianId(@PathVariable Integer physicianId) {
		return ResponseEntity.ok(affiliatedWithService.getByPhysicianId(physicianId));
	}

	@GetMapping("/department/{departmentId}")
	public ResponseEntity<List<AffiliatedWithDTO>> getByDepartmentId(@PathVariable Integer departmentId) {
		return ResponseEntity.ok(affiliatedWithService.getByDepartmentId(departmentId));
	}

	@GetMapping("/primary")
	public ResponseEntity<List<AffiliatedWithDTO>> getPrimaryAffiliations() {
		return ResponseEntity.ok(affiliatedWithService.getPrimaryAffiliations());
	}

	@GetMapping("/primary/{physicianId}")
	public ResponseEntity<DepartmentDTO> getPrimaryDepartment(@PathVariable Integer physicianId) {
		return ResponseEntity.ok(affiliatedWithService.getPrimaryDepartment(physicianId));
	}

//    @GetMapping("/exists/{physicianId}/{departmentId}")
//    public ResponseEntity<Boolean> exists(@PathVariable Integer physicianId,
//                                         @PathVariable Integer departmentId) {
//
//        AffiliatedWithId id = new AffiliatedWithId();
//        id.setPhysicianId(physicianId);
//        id.setDepartmentId(departmentId);
//
//        return ResponseEntity.ok(affiliatedWithService.exists(id));
//    }
//
//    @GetMapping("/count")
//    public ResponseEntity<Long> count() {
//        return ResponseEntity.ok(affiliatedWithService.count());
//    }
}