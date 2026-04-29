package com.cg.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;

public interface UndergoesService {
	// Get all undergoes records
	public List<Undergoes> getAllUndergoes();

	// Get by composite primary key
	public Optional<Undergoes> getUndergoesById(UndergoesId id);

	// Get all procedures undergone by a patient
	public List<Undergoes> getUndergoesByPatient(Long patientId);

	// Get all patient records for a specific procedure
	public List<Undergoes> getUndergoesByProcedure(Integer procedureCode);

	// Get all procedures under a specific hospital stay
	public List<Undergoes> getUndergoesByStay(Integer stayId);

	// Get all procedures performed by a physician
	public List<Undergoes> getUndergoesByPhysician(Integer physicianId);

	// Get all procedures assisted by a nurse
	public List<Undergoes> getUndergoesByAssistingNurse(Integer nurseId);

	// Get records by exact undergo date
	public List<Undergoes> getUndergoesByDate(LocalDateTime date);

	// Get records between date range
	public List<Undergoes> getUndergoesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

	public Undergoes saveUndergoes(Undergoes undergoes);
}
