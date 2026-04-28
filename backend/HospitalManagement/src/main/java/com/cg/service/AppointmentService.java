package com.cg.service;

import com.cg.entity.Appointment;
import java.util.List;

public interface AppointmentService {

    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(Integer id);

    List<Appointment> getByPatientId(Long patientId);

    List<Appointment> getByPhysicianId(Integer physicianId);

    List<Appointment> getByNurseId(Integer nurseId);

    List<Appointment> getByRoom(String room);
    
    Appointment save(Appointment a);
}