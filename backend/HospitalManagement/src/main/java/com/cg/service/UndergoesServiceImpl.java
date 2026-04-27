package com.cg.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;
import com.cg.repo.UndergoesRepository;

public class UndergoesServiceImpl implements UndergoesService {
	@Autowired
	private UndergoesRepository undergoesRepository;

	@Override
	public List<Undergoes> getAllUndergoes() {
		return undergoesRepository.findAll();
	}

	@Override
	public Optional<Undergoes> getUndergoesById(UndergoesId id) {
		return undergoesRepository.findById(id);
	}

	@Override
	public List<Undergoes> getUndergoesByPatient(Integer patientId) {
		return undergoesRepository.findByPatient_Ssn(patientId);
	}

	@Override
	public List<Undergoes> getUndergoesByProcedure(Integer procedureCode) {
		return undergoesRepository.findByProcedures_Code(procedureCode);
	}

	@Override
	public List<Undergoes> getUndergoesByStay(Integer stayId) {
		return undergoesRepository.findByStay_StayId(stayId);
	}

	@Override
	public List<Undergoes> getUndergoesByPhysician(Integer physicianId) {
		return undergoesRepository.findByPhysician_EmployeeID(physicianId);
	}

	@Override
	public List<Undergoes> getUndergoesByAssistingNurse(Integer nurseId) {
		return undergoesRepository.findByAssistingNurse_EmployeeID(nurseId);
	}

	@Override
	public List<Undergoes> getUndergoesByDate(LocalDateTime date) {
		return undergoesRepository.findById_DateUndergoes(date);
	}

	@Override
	public List<Undergoes> getUndergoesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
		return undergoesRepository.findById_DateUndergoesBetween(startDate, endDate);
	}
}
