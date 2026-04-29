package com.cg.repo;

import com.cg.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // based on relationships

    List<Appointment> findByPatient_Ssn(Long patientId);

    List<Appointment> findByPhysician_EmployeeId(Integer physicianId);

    List<Appointment> findByPrepNurse_EmployeeId(Integer nurseId);
    

    // based on fields

    List<Appointment> findByExaminationRoom(String examinationRoom);
}