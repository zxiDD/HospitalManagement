package com.cg.dto;

import java.time.LocalDateTime;

public class UndergoesDTO {

	private Long patientId;
	private String patientName;

	private Integer procedureCode;
	private String procedureName;

	private Integer stayId;

	private LocalDateTime dateUndergoes;

	private Integer physicianId;
	private String physicianName;

	private Integer assistingNurseId;
	private String nurseName;

	public UndergoesDTO() {
	}

	public UndergoesDTO(Long patientId, String patientName, Integer procedureCode, String procedureName, Integer stayId, LocalDateTime dateUndergoes,
			Integer physicianId, String physicianName, Integer assistingNurseId, String nurseName) {
		this.patientId = patientId;
		this.patientName = patientName;
		this.procedureCode = procedureCode;
		this.procedureName = procedureName;
		this.stayId = stayId;
		this.dateUndergoes = dateUndergoes;
		this.physicianId = physicianId;
		this.physicianName = physicianName;
		this.assistingNurseId = assistingNurseId;
		this.nurseName = nurseName;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() { return patientName; }
	public void setPatientName(String patientName) { this.patientName = patientName; }

	public Integer getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(Integer procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getProcedureName() { return procedureName; }
	public void setProcedureName(String procedureName) { this.procedureName = procedureName; }

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

	public String getPhysicianName() { return physicianName; }
	public void setPhysicianName(String physicianName) { this.physicianName = physicianName; }

	public Integer getAssistingNurseId() {
		return assistingNurseId;
	}

	public void setAssistingNurseId(Integer assistingNurseId) {
		this.assistingNurseId = assistingNurseId;
	}

	public String getNurseName() { return nurseName; }
	public void setNurseName(String nurseName) { this.nurseName = nurseName; }
}