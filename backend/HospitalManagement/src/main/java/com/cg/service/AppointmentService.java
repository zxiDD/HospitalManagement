package com.cg.service;

import com.cg.entity.Appointment;
import java.util.List;

public interface AppointmentService {

    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(int id);

    List<Appointment> getByPatientId(int patientId);

    List<Appointment> getByPhysicianId(int physicianId);

    List<Appointment> getByNurseId(int nurseId);

    List<Appointment> getByRoom(String room);
}