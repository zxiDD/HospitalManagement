package com.cg.service;

import com.cg.entity.Appointment;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.IllegalOperationException;
import com.cg.exception.ResourceNotFoundException;
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
    public Appointment getAppointmentById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }

    @Override
    public List<Appointment> getByPatientId(Long patientId) {
        return repository.findByPatient_Ssn(patientId);
    }

    @Override
    public List<Appointment> getByPhysicianId(Integer physicianId) {
        return repository.findByPhysician_EmployeeId(physicianId);
    }

    @Override
    public List<Appointment> getByNurseId(Integer nurseId) {
        return repository.findByPrepNurse_EmployeeId(nurseId);
    }

    @Override
    public List<Appointment> getByRoom(String room) {
        return repository.findByExaminationRoom(room);
    }
    
    @Override
    public List<Appointment> getAppointmentsByPhysician(Integer physicianId) {

        List<Appointment> list = repository.findByPhysician_EmployeeId(physicianId);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }

        return list;
    }
    
    @Override
    public List<String> getPatientsByPhysician(Integer physicianId) {

        List<Appointment> list = repository.findByPhysician_EmployeeId(physicianId);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No patients found");
        }

        return list.stream()
                .map(a -> a.getPatient().getName())
                .distinct()
                .toList();
    }
    
    @Override
    public Appointment save(Appointment a) {

        if (a.getAppointmentID() == null) {
            throw new BadRequestException("Appointment ID cannot be null");
        }

        if (repository.existsById(a.getAppointmentID())) {
            throw new DuplicateResourceException("Appointment already exists");
        }

        if (a.getEndo().isBefore(a.getStarto())) {
            throw new IllegalOperationException("End time cannot be before start time");
        }

        return repository.save(a);
    }
}