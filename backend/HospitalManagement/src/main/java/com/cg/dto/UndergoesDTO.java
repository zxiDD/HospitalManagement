package com.cg.dto;

import java.time.LocalDateTime;

public class UndergoesDTO {

	private Integer patientId;

	private Integer procedureCode;

	private Integer stayId;

	private LocalDateTime dateUndergoes;

	private Integer physicianId;

	private Integer assistingNurseId;

	public UndergoesDTO() {
	}

	public UndergoesDTO(Integer patientId, Integer procedureCode, Integer stayId, LocalDateTime dateUndergoes,
			Integer physicianId, Integer assistingNurseId) {
		this.patientId = patientId;
		this.procedureCode = procedureCode;
		this.stayId = stayId;
		this.dateUndergoes = dateUndergoes;
		this.physicianId = physicianId;
		this.assistingNurseId = assistingNurseId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(Integer procedureCode) {
		this.procedureCode = procedureCode;
	}

	public Integer getStayId() {
		return stayId;
	}

	public void setStayId(Integer stayId) {
		this.stayId = stayId;
	}

	public LocalDateTime getDateUndergoes() {
		return dateUndergoes;
	}

	public void setDateUndergoes(LocalDateTime dateUndergoes) {
		this.dateUndergoes = dateUndergoes;
	}

	public Integer getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}

	public Integer getAssistingNurseId() {
		return assistingNurseId;
	}

	public void setAssistingNurseId(Integer assistingNurseId) {
		this.assistingNurseId = assistingNurseId;
	}
}