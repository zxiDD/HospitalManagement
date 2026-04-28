package com.cg.controller;

import com.cg.entity.Appointment;
import com.cg.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @GetMapping
    public List<Appointment> getAll() {
        return service.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Integer id) {
        return service.getAppointmentById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }

    @GetMapping("/physician/{physicianId}")
    public List<Appointment> getByPhysician(@PathVariable Integer physicianId) {
        return service.getByPhysicianId(physicianId);
    }

    @GetMapping("/nurse/{nurseId}")
    public List<Appointment> getByNurse(@PathVariable Integer nurseId) {
        return service.getByNurseId(nurseId);
    }

    @GetMapping("/room/{room}")
    public List<Appointment> getByRoom(@PathVariable String room) {
        return service.getByRoom(room);
    }
}