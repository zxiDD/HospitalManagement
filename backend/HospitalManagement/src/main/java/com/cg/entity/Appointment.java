package com.cg.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Appointment {

    @Id
    private int appointmentID;

    @ManyToOne
    @JoinColumn(name = "patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "physician")
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "prepNurse")
    private Nurse prepNurse;

    private LocalDateTime starto;
    private LocalDateTime endo;
    private String examinationRoom;
	public int getAppointmentID() {
		return appointmentID;
	}
	public void setAppointmentID(int appointmentID) {
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
}