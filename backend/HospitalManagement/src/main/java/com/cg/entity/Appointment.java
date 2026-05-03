package com.cg.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "appointmentid")
	private Integer appointmentID;

	@ManyToOne
	@JoinColumn(name = "patient", referencedColumnName = "ssn")
	private Patient patient;

	@ManyToOne
	@JoinColumn(name = "physician", referencedColumnName = "employeeid")
	private Physician physician;

	@ManyToOne
	@JoinColumn(name = "prepnurse", referencedColumnName = "employeeid")
	private Nurse prepNurse;

	@Column(name = "starto")
	private LocalDateTime starto;

	@Column(name = "endo")
	private LocalDateTime endo;

	@Column(name = "examinationroom")
	private String examinationRoom;

	@Column(name = "is_active")
	private boolean isActive = true;

	public Appointment() {
	}

	public Appointment(Integer appointmentID, Patient patient, Physician physician, Nurse prepNurse,
			LocalDateTime starto, LocalDateTime endo, String examinationRoom) {
		this.appointmentID = appointmentID;
		this.patient = patient;
		this.physician = physician;
		this.prepNurse = prepNurse;
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

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Physician getPhysician() {
		return physician;
	}

	public void setPhysician(Physician physician) {
		this.physician = physician;
	}

	public Nurse getPrepNurse() {
		return prepNurse;
	}

	public void setPrepNurse(Nurse prepNurse) {
		this.prepNurse = prepNurse;
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}