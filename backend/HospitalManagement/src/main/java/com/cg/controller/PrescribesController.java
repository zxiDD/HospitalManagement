package com.cg.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.PrescribesDTO;
import com.cg.entity.Appointment;
import com.cg.entity.Medication;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import com.cg.service.AppointmentService;
import com.cg.service.MedicationService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;
import com.cg.service.PrescribesService;

@RestController
public class PrescribesController {

	private final PrescribesService service;
	private final PhysicianService physicianService;
	private final PatientService patientService;
	private final MedicationService medicationService;
	private final AppointmentService appointmentService;

	public PrescribesController(PrescribesService service, PhysicianService physicianService,
			PatientService patientService, MedicationService medicationService, AppointmentService appointmentService) {
		this.service = service;
		this.physicianService = physicianService;
		this.patientService = patientService;
		this.medicationService = medicationService;
		this.appointmentService = appointmentService;
	}

	private PrescribesDTO mapToDTO(Prescribes p) {
		return new PrescribesDTO(p.getPhysician().getEmployeeId(), p.getPatient().getSsn(), p.getMedication().getCode(),
				p.getId().getDate(), p.getDose(),
				p.getAppointment() != null ? p.getAppointment().getAppointmentID() : null);
	}

	@GetMapping("/prescribes")
	public ResponseEntity<List<PrescribesDTO>> getAll() {
		List<PrescribesDTO> list = service.getAll().stream().map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/prescribes/id")
	public ResponseEntity<PrescribesDTO> getById(@RequestParam Integer physicianId, @RequestParam Long patientSsn,
			@RequestParam Integer medicationId, @RequestParam String date // pass as ISO string
	) {

		PrescribesId id = new PrescribesId(physicianId, patientSsn, medicationId, java.time.LocalDateTime.parse(date));

		Prescribes p = service.getById(id);

		return ResponseEntity.ok(mapToDTO(p));
	}

	@GetMapping("/prescribes/count")
	public ResponseEntity<Long> count() {
		return ResponseEntity.ok(service.count());
	}

	@GetMapping("/prescribes/sorted")
	public ResponseEntity<List<PrescribesDTO>> getAllSorted() {
		List<PrescribesDTO> list = service.getAllSorted().stream().map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/prescribes/physician/{physicianId}")
	public ResponseEntity<List<PrescribesDTO>> getByPhysician(@PathVariable Integer physicianId) {

		List<PrescribesDTO> list = service.getAll().stream()
				.filter(p -> p.getPhysician().getEmployeeId().equals(physicianId)).map(this::mapToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@GetMapping("/prescribes/patient/{ssn}")
	public ResponseEntity<List<PrescribesDTO>> getByPatient(@PathVariable Long ssn) {

		List<PrescribesDTO> list = service.getAll().stream().filter(p -> p.getPatient().getSsn().equals(ssn))
				.map(this::mapToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	@PostMapping("/admin/prescribes")
	public ResponseEntity<PrescribesDTO> create(@RequestBody PrescribesDTO dto) {

		if (dto.getPhysicianId() == null || dto.getPatientSsn() == null || dto.getMedicationId() == null
				|| dto.getDate() == null) {

			return ResponseEntity.badRequest().build();
		}

		PrescribesId id = new PrescribesId();
		id.setPhysician(dto.getPhysicianId());
		id.setPatient(dto.getPatientSsn());
		id.setMedication(dto.getMedicationId());
		id.setDate(dto.getDate());

		Physician physician = new Physician();
		physician.setEmployeeId(dto.getPhysicianId());

		Patient patient = new Patient();
		patient.setSsn(dto.getPatientSsn());

		Medication medication = new Medication();
		medication.setCode(dto.getMedicationId());

		Appointment appointment = null;
		if (dto.getAppointmentId() != null) {
			appointment = new Appointment();
			appointment.setAppointmentID(dto.getAppointmentId());
		}

		Prescribes prescribes = new Prescribes();
		prescribes.setId(id);
		prescribes.setPhysician(physician);
		prescribes.setPatient(patient);
		prescribes.setMedication(medication);
		prescribes.setDose(dto.getDose());
		prescribes.setAppointment(appointment);

		Prescribes saved = service.save(prescribes);

		PrescribesDTO response = new PrescribesDTO(saved.getPhysician().getEmployeeId(), saved.getPatient().getSsn(),
				saved.getMedication().getCode(), saved.getId().getDate(), saved.getDose(),
				saved.getAppointment() != null ? saved.getAppointment().getAppointmentID() : null);

		return ResponseEntity.status(201).body(response);
	}

}