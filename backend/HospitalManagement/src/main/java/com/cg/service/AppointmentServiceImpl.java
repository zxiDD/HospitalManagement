package com.cg.service;

import com.cg.entity.Appointment;
import com.cg.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Override
    public List<Appointment> getAllAppointments() {
        return repository.findAll();
    }

    @Override
    public Appointment getAppointmentById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Appointment> getByPatientId(int patientId) {
        return repository.findByPatient_PatientId(patientId);
    }

    @Override
    public List<Appointment> getByPhysicianId(int physicianId) {
        return repository.findByPhysician_PhysicianId(physicianId);
    }

    @Override
    public List<Appointment> getByNurseId(int nurseId) {
        return repository.findByPrepNurse_NurseId(nurseId);
    }

    @Override
    public List<Appointment> getByRoom(String room) {
        return repository.findByExaminationRoom(room);
    }
}