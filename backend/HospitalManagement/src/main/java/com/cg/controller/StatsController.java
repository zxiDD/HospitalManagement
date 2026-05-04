package com.cg.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.repo.PatientRepository;
import com.cg.repo.PhysicianRepository;
import com.cg.repo.NurseRepository;
import com.cg.repo.AppointmentRepository;
import com.cg.repo.DepartmentRepository;
import com.cg.repo.RoomRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/stats")
@SecurityRequirement(name = "BearerAuth")
public class StatsController {

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private PhysicianRepository physicianRepository;

	@Autowired
	private NurseRepository nurseRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private RoomRepository roomRepository;

	@GetMapping("/counts")
	public ResponseEntity<Map<String, Long>> getCounts() {
		Map<String, Long> counts = new LinkedHashMap<>();
		counts.put("patients", patientRepository.count());
		counts.put("physicians", physicianRepository.count());
		counts.put("nurses", nurseRepository.count());
		counts.put("appointments", appointmentRepository.count());
		counts.put("departments", departmentRepository.count());
		counts.put("rooms", roomRepository.count());
		return ResponseEntity.ok(counts);
	}
}
