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
	
	List<Appointment> findByIsActiveTrue();
	
	@Query("""
	        SELECT COUNT(a) > 0
	        FROM Appointment a
	        WHERE a.prepNurse.employeeId = :nurseId
	          AND a.appointmentID <> :excludeId
	          AND a.starto < :end
	          AND a.endo   > :start
	          AND a.isActive = true
	    """)
	    boolean isNurseBookedDuring(
	            @Param("nurseId")    Integer nurseId,
	            @Param("start")      LocalDateTime start,
	            @Param("end")        LocalDateTime end,
	            @Param("excludeId")  Integer excludeId
	    );

	@Query("""
	        SELECT COUNT(a) > 0
	        FROM Appointment a
	        WHERE a.examinationRoom = :room
	          AND (:excludeId IS NULL OR a.appointmentID <> :excludeId)
	          AND a.starto < :end
	          AND a.endo   > :start
	          AND a.isActive = true
	    """)
	boolean isRoomBookedDuring(
	            @Param("room")        String room,
	            @Param("start")       LocalDateTime start,
	            @Param("end")         LocalDateTime end,
	            @Param("excludeId")   Integer excludeId
	);

	@Query("""
	        SELECT COUNT(a) > 0
	        FROM Appointment a
	        WHERE a.physician.employeeId = :physicianId
	          AND (:excludeId IS NULL OR a.appointmentID <> :excludeId)
	          AND a.starto < :end
	          AND a.endo   > :start
	          AND a.isActive = true
	    """)
	boolean isPhysicianBookedDuring(
	            @Param("physicianId") Integer physicianId,
	            @Param("start")       LocalDateTime start,
	            @Param("end")         LocalDateTime end,
	            @Param("excludeId")   Integer excludeId
	);

	@Query("""
	        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
	        FROM Appointment a
	        WHERE a.patient.ssn = :patientId
	          AND FUNCTION('DATE', a.starto) = :date
	          AND a.isActive = true
	    """)
	boolean existsAppointmentForPatientOnDate(@Param("patientId") Long patientId, @Param("date") java.time.LocalDate date);

	@Query("""
	        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
	        FROM Appointment a
	        WHERE a.patient.ssn = :patientId
	          AND FUNCTION('DATE', a.starto) = :date
	          AND a.appointmentID <> :excludeId
	          AND a.isActive = true
	    """)
	boolean existsAppointmentForPatientOnDateExcluding(@Param("patientId") Long patientId, @Param("date") java.time.LocalDate date, @Param("excludeId") Integer excludeId);
}