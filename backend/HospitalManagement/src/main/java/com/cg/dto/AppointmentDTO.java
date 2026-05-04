package com.cg.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppointmentDTO {
	private Integer appointmentID;

	@NotNull(message = "Patient ID is required")
	@Min(value = 1, message = "Patient ID must be positive")
	private Integer patientId;
	private String patientName;

	@NotNull(message = "Physician ID is required")
	@Min(value = 1, message = "Physician ID must be positive")
	private Integer physicianId;
	private String physicianName;

	private Integer nurseId;
	private String nurseName;

	@NotNull(message = "Start time is required")
	@FutureOrPresent(message = "Start time must be present or future")
	private LocalDateTime starto;

	@NotNull(message = "End time is required")
	@Future(message = "End time must be future")
	private LocalDateTime endo;

	@NotBlank(message = "Room cannot be empty")
	@Size(min = 2, max = 20, message = "Room must be 2–20 characters")
	private String examinationRoom;

	public AppointmentDTO() {
	}

	public AppointmentDTO(Integer appointmentID, Integer patientId, String patientName, Integer physicianId, String physicianName, Integer nurseId, String nurseName,
			LocalDateTime starto, LocalDateTime endo, String examinationRoom) {
		super();
		this.appointmentID = appointmentID;
		this.patientId = patientId;
		this.patientName = patientName;
		this.physicianId = physicianId;
		this.physicianName = physicianName;
		this.nurseId = nurseId;
		this.nurseName = nurseName;
		this.starto = starto;
		this.endo = endo;
		this.examinationRoom = examinationRoom;
	}

	public Integer getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(Integer appointmentID) {
		this.appointmentID = appointmentID;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}

	public String getPhysicianName() {
		return physicianName;
	}

	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public LocalDateTime getStarto() {
		return starto;
	}

	public void setStarto(LocalDateTime starto) {
		this.starto = starto;
	}

	public LocalDateTime getEndo() {
		return endo;
	}

	public void setEndo(LocalDateTime endo) {
		this.endo = endo;
	}

	public String getExaminationRoom() {
		return examinationRoom;
	}

	public void setExaminationRoom(String examinationRoom) {
		this.examinationRoom = examinationRoom;
	}

}