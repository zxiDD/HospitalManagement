package com.cg.controller;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.PatientDTO;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.ValidationException;
import com.cg.repo.PatientRepository;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patients")
@SecurityRequirement(name = "BearerAuth")
public class PatientController {

	private final PatientService service;
	private final PhysicianService physicianService;

	public PatientController(PatientService service, PhysicianService physicianService) {
		this.service = service;
		this.physicianService = physicianService;
	}

	private PatientDTO mapToDTO(Patient p) {
		return new PatientDTO(p.getSsn(), p.getName(), p.getAddress(), p.getPhone(), p.getInsuranceId(),
				p.getPhysician() != null ? p.getPhysician().getEmployeeId() : null);
	}

	@GetMapping
	public ResponseEntity<List<PatientDTO>> getAll() {
		List<PatientDTO> list = service.getAll().stream().map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/{ssn}")
	public ResponseEntity<PatientDTO> getBySsn(@PathVariable Long ssn) {
		Patient p = service.getById(ssn);
		return ResponseEntity.ok(mapToDTO(p));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<PatientDTO>> getByName(@PathVariable String name) {
		List<PatientDTO> list = service.getByName(name).stream().map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/search")
	public ResponseEntity<List<PatientDTO>> getByNameAndAddress(@RequestParam String name,
			@RequestParam String address) {

		List<PatientDTO> list = service.getByNameAndAddress(name, address).stream().map(this::mapToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

//    @GetMapping("/sorted")
//    public ResponseEntity<List<PatientDTO>> getAllSorted() {
//        List<PatientDTO> list = service.getAllSorted()
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

//    @GetMapping("/exists/{ssn}")
//    public ResponseEntity<Boolean> exists(@PathVariable Long ssn) {
//        return ResponseEntity.ok(service.exists(ssn));
//    }

	@PostMapping
	public ResponseEntity<PatientDTO> create(@Valid @RequestBody PatientDTO dto, BindingResult br) {

		if (br.hasErrors()) {
			throw new ValidationException(br.getFieldErrors());
		}

		if (dto.getPhysicianId() == null) {
			throw new BadRequestException("Physician ID is required");
		}

		Physician physician = physicianService.getById(dto.getPhysicianId());

		Patient patient = new Patient();
		patient.setSsn(dto.getSsn());
		patient.setName(dto.getName());
		patient.setAddress(dto.getAddress());
		patient.setPhone(dto.getPhone());
		patient.setInsuranceId(dto.getInsuranceId());
		patient.setPhysician(physician);

		Patient saved = service.save(patient);

		return ResponseEntity.status(201).body(mapToDTO(saved));
	}

	@PutMapping("/{ssn}")
	public ResponseEntity<PatientDTO> update(@PathVariable Long ssn, @Valid @RequestBody PatientDTO dto) {

		Patient existing = service.getById(ssn);

		existing.setName(dto.getName());
		existing.setAddress(dto.getAddress());
		existing.setPhone(dto.getPhone());
		existing.setInsuranceId(dto.getInsuranceId());

		if (dto.getPhysicianId() != null) {
			Physician physician = physicianService.getById(dto.getPhysicianId());
			existing.setPhysician(physician);
		}

		Patient updated = service.save(existing);

		return ResponseEntity.ok(mapToDTO(updated));
	}

	@DeleteMapping("/{ssn}")
	public ResponseEntity<Void> delete(@PathVariable Long ssn) {

		service.delete(ssn); // soft delete

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/phone/{phone}")
	public ResponseEntity<PatientDTO> getByPhone(@PathVariable String phone) {
		Patient patient = service.getByPhone(phone)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with phone number: " + phone));

		return ResponseEntity.ok(mapToDTO(patient));
	}
}