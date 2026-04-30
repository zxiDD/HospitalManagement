package com.cg.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.NurseDTO;
import com.cg.dto.PatientDTO;
import com.cg.entity.Appointment;
import com.cg.entity.Nurse;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.IllegalOperationException;
import com.cg.exception.ValidationException;
import com.cg.repo.PatientRepository;
import com.cg.repo.PhysicianRepository;
import com.cg.service.AppointmentService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "BearerAuth")
public class AppointmentController {

	@Autowired
	private AppointmentService service;

	@Autowired
	private PatientService patientService;

	@Autowired
	private PhysicianService physicianService;

	private AppointmentDTO convertToDTO(Appointment a) {
		if (a == null)
			return null;

		return new AppointmentDTO(a.getAppointmentID(),
				a.getPatient() != null ? a.getPatient().getSsn().intValue() : null,
				a.getPhysician() != null ? a.getPhysician().getEmployeeId() : null,
				a.getPrepNurse() != null ? a.getPrepNurse().getEmployeeId() : null, a.getStarto(), a.getEndo(),
				a.getExaminationRoom());
	}

	private PatientDTO convertToDTO(Patient p) {
		return new PatientDTO(p.getSsn(), p.getName(), p.getAddress(), p.getPhone(), p.getInsuranceId(),
				p.getPhysician() != null ? p.getPhysician().getEmployeeId() : null);
	}

	@GetMapping("/appointments")
	public ResponseEntity<List<AppointmentDTO>> getAll() {
		List<AppointmentDTO> list = service.getAllAppointments().stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/appointments/{id}")
	public ResponseEntity<AppointmentDTO> getById(@PathVariable Integer id) {
		Appointment a = service.getAppointmentById(id);

		if (a == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(convertToDTO(a));
	}

	@GetMapping("/appointments/physician/{physicianId}/patients")
	public ResponseEntity<List<PatientDTO>> getPatientsByPhysician(@PathVariable Integer physicianId) {
		List<Patient> patients = service.getPatientsByPhysician(physicianId);
		List<PatientDTO> patientsDTO = patients.stream().map(this::convertToDTO).toList();
		return ResponseEntity.ok(patientsDTO);
	}

	@GetMapping("/appointments/physician/{physicianId}/appointments")
	public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPhysician(@PathVariable Integer physicianId) {

		List<AppointmentDTO> list = service.getByPhysicianId(physicianId).stream().map(this::convertToDTO).toList();

		return ResponseEntity.ok(list);
	}

	@PostMapping("/appointments")
	public ResponseEntity<?> addAppointment(@Valid @RequestBody AppointmentDTO dto, BindingResult br) {
		if (br.hasErrors()) {
			throw new ValidationException(br.getFieldErrors());
		}
		if (dto.getPatientId() == null) {
			throw new BadRequestException("Patient ID is required");
		}
		if (dto.getPhysicianId() == null) {
			throw new BadRequestException("Physician ID is required");
		}
		if (dto.getEndo().isBefore(dto.getStarto())) {
			throw new BadRequestException("End time must be after start time");
		}
		Patient patient = patientService.getById(dto.getPatientId().longValue());
		Physician physician = physicianService.getById(dto.getPhysicianId());
		Appointment appointment = new Appointment();
		appointment.setStarto(dto.getStarto());
		appointment.setEndo(dto.getEndo());
		appointment.setExaminationRoom(dto.getExaminationRoom());
		appointment.setPatient(patient);
		appointment.setPhysician(physician);
		Appointment saved = service.save(appointment);
		if (dto.getNurseId() != null) {
			saved = service.assignPrepNurse(saved.getAppointmentID(), dto.getNurseId());
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
	}

	@PostMapping("/appointments/reschedule")
	public ResponseEntity<?> reschedule(@Valid @RequestBody AppointmentDTO dto, BindingResult br) {

		if (br.hasErrors()) {
			throw new ValidationException(br.getFieldErrors());
		}

		Appointment a = service.getAppointmentById(dto.getAppointmentID());

		if (dto.getEndo().isBefore(dto.getStarto())) {
			throw new IllegalOperationException("Invalid time range");
		}

		if (dto.getStarto().isBefore(java.time.LocalDateTime.now())) {
			throw new IllegalOperationException("Cannot reschedule to past time");
		}

		a.setStarto(dto.getStarto());
		a.setEndo(dto.getEndo());

		return ResponseEntity.ok(service.save(a));
	}

	@GetMapping("/appointments/patients/{patientId}/appointments")
	public ResponseEntity<List<AppointmentDTO>> getAppointmentsForPatient(@PathVariable Long patientId) {

		List<Appointment> appointments = service.getByPatientId(patientId);
		List<AppointmentDTO> appointmentsDTO = appointments.stream().map(this::convertToDTO).toList();
		return ResponseEntity.ok(appointmentsDTO);
	}

	@PatchMapping("/appointments/{appointmentId}/{nurseId}")
	public ResponseEntity<AppointmentDTO> assignPrepNurse(@PathVariable Integer appointmentId,
			@PathVariable Integer nurseId) {

		Appointment updated = service.assignPrepNurse(appointmentId, nurseId);
		return ResponseEntity.ok(convertToDTO(updated));
	}

}