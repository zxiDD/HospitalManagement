package com.cg.controller;

import com.cg.dto.AppointmentDTO;
import com.cg.dto.NurseDTO;
import com.cg.entity.*;
import com.cg.exception.BadRequestException;
import com.cg.exception.IllegalOperationException;
import com.cg.service.AppointmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;


    private AppointmentDTO convertToDTO(Appointment a) {
        if (a == null) return null;

        return new AppointmentDTO(
                a.getAppointmentID(),
                a.getPatient() != null ? a.getPatient().getSsn().intValue() : null,
                a.getPhysician() != null ? a.getPhysician().getEmployeeId() : null,
                a.getPrepNurse() != null ? a.getPrepNurse().getEmployeeId() : null,
                a.getStarto(),
                a.getEndo(),
                a.getExaminationRoom()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Integer id) {

        service.cancelAppointment(id);

        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        List<AppointmentDTO> list = service.getAllAppointments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Integer id) {
        Appointment a = service.getAppointmentById(id);
        return ResponseEntity.ok(convertToDTO(a));
    }
    
    @GetMapping("/physician/{physicianId}/patients")
    public ResponseEntity<List<String>> getPatientsByPhysician(@PathVariable Integer physicianId) {
        return ResponseEntity.ok(service.getPatientsByPhysician(physicianId));
    }
    
    @GetMapping("/physician/{physicianId}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPhysician(
            @PathVariable Integer physicianId) {

        List<AppointmentDTO> list = service.getAppointmentsByPhysician(physicianId)
                .stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(list);
    }


    @PostMapping
    public ResponseEntity<?> addAppointment(@Valid @RequestBody AppointmentDTO dto) {

        if (dto.getEndo().isBefore(dto.getStarto())) {
            throw new BadRequestException("End time must be after start time");
        }

        Appointment a = new Appointment();


        a.setAppointmentID(dto.getAppointmentID());
        a.setStarto(dto.getStarto());
        a.setEndo(dto.getEndo());
        a.setExaminationRoom(dto.getExaminationRoom());

        Patient p = new Patient();
        p.setSsn(dto.getPatientId().longValue());
        a.setPatient(p);

        Physician ph = new Physician();
        ph.setEmployeeId(dto.getPhysicianId());
        a.setPhysician(ph);

        Nurse n = new Nurse();
        n.setEmployeeId(dto.getNurseId());
        a.setPrepNurse(n);

        return ResponseEntity.status(201).body(service.save(a));
    }

    @PostMapping("/reschedule")
    public ResponseEntity<?> reschedule(@Valid @RequestBody AppointmentDTO dto) {

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
    
    @GetMapping("/patients/{patientId}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForPatient(
            @PathVariable Long patientId) {
 
        List<Appointment> appointments = service.getByPatientId(patientId);
        List<AppointmentDTO> appointmentsDTO = appointments.stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(appointmentsDTO);
    }
    
    @PatchMapping("/appointments/{appointmentId}/nurse")
    public ResponseEntity<AppointmentDTO> assignPrepNurse(
            @PathVariable Integer appointmentId,
            @Valid @RequestBody NurseDTO request) {
 
        Appointment updated = service.assignPrepNurse(appointmentId, request.getEmployeeId());
        return ResponseEntity.ok(convertToDTO(updated));
    }
}