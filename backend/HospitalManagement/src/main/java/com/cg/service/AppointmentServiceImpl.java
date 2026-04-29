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
    public void cancelAppointment(Integer id) {

        Appointment appointment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found with ID: " + id));

        repository.delete(appointment);
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
    public Appointment save(Appointment a) {

        if (a.getEndo().isBefore(a.getStarto())) {
            throw new IllegalOperationException("End time cannot be before start time");
        }

        return repository.save(a);
    }
}