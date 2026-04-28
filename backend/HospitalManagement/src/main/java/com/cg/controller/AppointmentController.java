package com.cg.controller;

import com.cg.dto.AppointmentDTO;
import com.cg.entity.*;
import com.cg.service.AppointmentService;

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

        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDTO(a));
    }


    @PostMapping
    public ResponseEntity<Appointment> addAppointment(@RequestBody AppointmentDTO dto) {

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

        Appointment saved = service.save(a);

        return ResponseEntity.status(201).body(saved);
    }


    @PostMapping("/reschedule")
    public ResponseEntity<Appointment> reschedule(@RequestBody AppointmentDTO dto) {

        Appointment a = service.getAppointmentById(dto.getAppointmentID());

        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        a.setStarto(dto.getStarto());
        a.setEndo(dto.getEndo());

        Appointment updated = service.save(a);

        return ResponseEntity.ok(updated);
    }
}