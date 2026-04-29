package com.cg.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppointmentDTO {


    @NotNull(message = "Appointment ID is required")
    @Min(value = 1, message = "Appointment ID must be positive")
    private Integer appointmentID;

    @NotNull(message = "Patient ID is required")
    @Min(value = 1, message = "Patient ID must be positive")
    private Integer patientId;

    @NotNull(message = "Physician ID is required")
    @Min(value = 1, message = "Physician ID must be positive")
    private Integer physicianId;

    @NotNull(message = "Nurse ID is required")
    @Min(value = 1, message = "Nurse ID must be positive")
    private Integer nurseId;

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
    

	public AppointmentDTO(Integer appointmentID, Integer patientId, Integer physicianId, Integer nurseId,
			LocalDateTime starto, LocalDateTime endo, String examinationRoom) {
		super();
		this.appointmentID = appointmentID;
		this.patientId = patientId;
		this.physicianId = physicianId;
		this.nurseId = nurseId;
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

	public Integer getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
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