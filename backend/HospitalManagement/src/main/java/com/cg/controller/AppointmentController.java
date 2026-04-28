package com.cg.controller;

import com.cg.dto.AppointmentDTO;
import com.cg.entity.Appointment;
import com.cg.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<AppointmentDTO> getAll() {
        return service.getAllAppointments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public AppointmentDTO getById(@PathVariable Integer id) {
        return convertToDTO(service.getAppointmentById(id));
    }


    @GetMapping("/patient/{patientId}")
    public List<AppointmentDTO> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/physician/{physicianId}")
    public List<AppointmentDTO> getByPhysician(@PathVariable Integer physicianId) {
        return service.getByPhysicianId(physicianId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @GetMapping("/nurse/{nurseId}")
    public List<AppointmentDTO> getByNurse(@PathVariable Integer nurseId) {
        return service.getByNurseId(nurseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/room/{room}")
    public List<AppointmentDTO> getByRoom(@PathVariable String room) {
        return service.getByRoom(room)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}