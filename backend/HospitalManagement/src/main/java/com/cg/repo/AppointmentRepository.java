package com.cg.repo;

import com.cg.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	// based on relationships

	List<Appointment> findByPatient_Ssn(Long patientId);

	List<Appointment> findByPhysician_EmployeeId(Integer physicianId);

	List<Appointment> findByPrepNurse_EmployeeId(Integer nurseId);

	// based on fields

	List<Appointment> findByExaminationRoom(String examinationRoom);

	public List<Appointment> findByPatient_SsnOrderByStartoDesc(Long ssn);
	
	@Query("""
	        SELECT COUNT(a) > 0
	        FROM Appointment a
	        WHERE a.prepNurse.employeeId = :nurseId
	          AND a.appointmentID <> :excludeId
	          AND a.starto < :end
	          AND a.endo   > :start
	    """)
	    boolean isNurseBookedDuring(
	            @Param("nurseId")    Integer nurseId,
	            @Param("start")      LocalDateTime start,
	            @Param("end")        LocalDateTime end,
	            @Param("excludeId")  Integer excludeId
	    );
}