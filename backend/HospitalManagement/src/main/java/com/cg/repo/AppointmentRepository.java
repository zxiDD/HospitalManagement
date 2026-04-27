package com.cg.repo;

import com.cg.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // based on relationships

    List<Appointment> findByPatient_PatientId(int patientId);

    List<Appointment> findByPhysician_PhysicianId(int physicianId);

    List<Appointment> findByPrepNurse_NurseId(int nurseId);

    // based on fields

    List<Appointment> findByExaminationRoom(String examinationRoom);
}