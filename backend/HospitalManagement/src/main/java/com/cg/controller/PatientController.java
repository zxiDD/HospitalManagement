package com.cg.controller;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.MedicationDTO;
import com.cg.dto.PatientDTO;
import com.cg.dto.PatientDashboardDTO;
import com.cg.dto.StayDTO;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.ValidationException;
import com.cg.repo.PatientRepository;
import com.cg.service.AppointmentService;
import com.cg.service.MedicationService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;
import com.cg.service.PrescribesService;
import com.cg.service.StayService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "BearerAuth")
public class PatientController {
	@Autowired
	private PatientService service;
	@Autowired
	private PhysicianService physicianService;
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PrescribesService prescribesService;
	@Autowired
	private StayService stayService;

	
	private PatientDTO mapToDTO(Patient p) {
		return new PatientDTO(p.getSsn(), p.getName(), p.getAddress(), p.getPhone(), p.getInsuranceId(),
				p.getPhysician() != null ? p.getPhysician().getEmployeeId() : null);
	}

	@GetMapping("/patients")
	public ResponseEntity<List<PatientDTO>> getAll() {
		List<PatientDTO> list = service.getAll().stream().map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/patients/{ssn}")
	public ResponseEntity<PatientDTO> getBySsn(@PathVariable Long ssn) {
		Patient p = service.getById(ssn);
		return ResponseEntity.ok(mapToDTO(p));
	}

	@GetMapping("patients/name/{name}")
	public ResponseEntity<List<PatientDTO>> getByName(@PathVariable String name) {
		List<PatientDTO> list = service.getByName(name).stream().map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/patients/search")
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

	@PostMapping("/admin/patients")
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

	@PutMapping("/admin/patients/{ssn}")
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

	@DeleteMapping("/admin/patients/{ssn}")
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

	@PatchMapping("/admin/patients/{ssn}/pcp/{physicianId}")
	public ResponseEntity<PatientDTO> assignPCP(@PathVariable Long ssn, @PathVariable Integer physicianId) {

		Patient patient = service.getById(ssn);

		Physician physician = physicianService.getById(physicianId);

		patient.setPhysician(physician);

		Patient updatedPatient = service.assignPCP(patient);

		return ResponseEntity.ok(mapToDTO(updatedPatient));
	}

	@GetMapping("/patients/{ssn}/dashboard")
	public ResponseEntity<PatientDashboardDTO> getPatientDashboard(@PathVariable Long ssn) {

		Patient patient = service.getById(ssn);

		PatientDTO patientSummary = new PatientDTO();
		patientSummary.setName(patient.getName());
		patientSummary.setSsn(patient.getSsn());
		patientSummary.setAddress(patient.getAddress());
		patientSummary.setInsuranceId(patient.getInsuranceId());
		patientSummary.setPhone(patient.getPhone());

		if (patient.getPhysician() != null) {
			patientSummary.setPhysicianId(patient.getPhysician().getEmployeeId());
		}

		List<AppointmentDTO> appointments = appointmentService.getUpcomingAppointmentsByPatient(ssn);

		StayDTO currentStay = stayService.getActiveStayByPatient(ssn);

		List<MedicationDTO> medications = prescribesService.getMedicationsByPatient(ssn);

		PatientDashboardDTO dashboard = new PatientDashboardDTO();
		dashboard.setPatient(patientSummary);
		dashboard.setAppointments(appointments);
		dashboard.setStay(currentStay);
		dashboard.setMedications(medications);

		return ResponseEntity.ok(dashboard);
	}
}