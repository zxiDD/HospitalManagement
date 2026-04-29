package com.cg.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;

public interface UndergoesRepository extends JpaRepository<Undergoes, UndergoesId> {
	// Find by patient
	List<Undergoes> findByPatient_Ssn(Long patientId);

	// Find by procedure
	List<Undergoes> findByProcedures_Code(Integer procedureCode);

	// Find by stay
	List<Undergoes> findByStay_StayId(Integer stayId);

	// Find by physician
	List<Undergoes> findByPhysician_EmployeeId(Integer physicianId);

	// Find by assisting nurse
	List<Undergoes> findByAssistingNurse_EmployeeId(Integer nurseId);

	// Find by exact date
	List<Undergoes> findById_DateUndergoes(LocalDateTime date);
	

	// Find by date range
	List<Undergoes> findById_DateUndergoesBetween(LocalDateTime startDate, LocalDateTime endDate);
}
