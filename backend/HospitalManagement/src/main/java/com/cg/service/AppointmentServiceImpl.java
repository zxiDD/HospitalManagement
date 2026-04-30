package com.cg.service;

import com.cg.entity.Appointment;
import com.cg.entity.Nurse;
import com.cg.entity.Patient;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.IllegalOperationException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AppointmentRepository;
import com.cg.repo.NurseRepository;
import com.cg.repo.PatientRepository;
import com.cg.repo.PhysicianRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository repository;

	@Autowired
	private PatientRepository patientRepo;

	@Autowired
	private NurseRepository nurseRepo;

	@Autowired
	private PhysicianRepository physicianRepo;

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
		patientRepo.findById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));

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

		if (a.getAppointmentID() != null && repository.existsById(a.getAppointmentID())) {
			throw new DuplicateResourceException("Appointment already exists");
		}

		if (a.getEndo().isBefore(a.getStarto())) {
			throw new IllegalOperationException("End time cannot be before start time");
		}

		return repository.save(a);
	}

	@Override
	public void cancelAppointment(Integer id) {

		Appointment appointment = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

		appointment.setActive(false);

		repository.save(appointment);
	}

	@Override
	public Appointment assignPrepNurse(Integer appointmentId, Integer nurseId) {

		Appointment appointment = repository.findById(appointmentId).orElseThrow(
				() -> new ResourceNotFoundException("appointment does not exist with id " + appointmentId));

		Nurse nurse = nurseRepo.findById(nurseId)
				.orElseThrow(() -> new ResourceNotFoundException("nurse not found with id " + nurseId));

		boolean isOnCall = nurseRepo.isNurseOnCallDuring(nurseId, appointment.getStarto(), appointment.getEndo());

		if (!isOnCall) {
			throw new BadRequestException(
					"Nurse " + nurse.getName() + " is not on-call during this appointment window.");
		}

		boolean alreadyBooked = repository.isNurseBookedDuring(nurseId, appointment.getStarto(), appointment.getEndo(),
				appointmentId);

		if (alreadyBooked) {
			throw new BadRequestException(
					"Nurse " + nurse.getName() + " is already assigned to another appointment at this time.");
		}

		appointment.setPrepNurse(nurse);
		Appointment saved = repository.save(appointment);
		return saved;
	}

	@Override
	public List<Patient> getPatientsByPhysician(Integer physicianId) {
		physicianRepo.findById(physicianId)
				.orElseThrow(() -> new ResourceNotFoundException("Physician not found with id " + physicianId));

		List<Patient> patients = patientRepo.findByPhysicianEmployeeId(physicianId);

		return patients;
	}
}