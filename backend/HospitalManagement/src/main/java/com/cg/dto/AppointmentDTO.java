package com.cg.dto;

import java.time.LocalDateTime;

public class AppointmentDTO {

    private Integer appointmentID;

    private Integer patientId;
    private Integer physicianId;
    private Integer nurseId;

    private LocalDateTime starto;
    private LocalDateTime endo;
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