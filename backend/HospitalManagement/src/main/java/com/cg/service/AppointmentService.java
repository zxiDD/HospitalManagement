package com.cg.service;

import com.cg.dto.AppointmentDTO;
import com.cg.entity.Appointment;
import com.cg.entity.Patient;

import java.util.List;

public interface AppointmentService {

	List<Appointment> getAllAppointments();

	Appointment getAppointmentById(Integer id);

	List<Appointment> getByPatientId(Long patientId);

	List<Appointment> getByPhysicianId(Integer physicianId);

	List<Appointment> getByNurseId(Integer nurseId);

	List<Appointment> getByRoom(String room);

	List<Patient> getPatientsByPhysician(Integer physicianId);

	Appointment save(Appointment a);

	public Appointment assignPrepNurse(Integer appointmentId, Integer nurseId);

	void cancelAppointment(Integer id);

	List<AppointmentDTO> getUpcomingAppointmentsByPatient(Long ssn);
}