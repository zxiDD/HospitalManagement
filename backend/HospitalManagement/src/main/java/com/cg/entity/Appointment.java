package com.cg.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Appointment")
public class Appointment {

    @Id
    @Column(name = "AppointmentID")
    private int appointmentID;

    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "patientID")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "physician", referencedColumnName = "EmployeeID")
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "prepNurse", referencedColumnName = "employeeID")
    private Nurse prepNurse;

    @Column(name = "Starto")
    private LocalDateTime starto;

    @Column(name = "Endo")
    private LocalDateTime endo;

    @Column(name = "ExaminationRoom")
    private String examinationRoom;

    public Appointment() {
    }

    public Appointment(int appointmentID, Patient patient, Physician physician, Nurse prepNurse,
                       LocalDateTime starto, LocalDateTime endo, String examinationRoom) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.physician = physician;
        this.prepNurse = prepNurse;
        this.starto = starto;
        this.endo = endo;
        this.examinationRoom = examinationRoom;
    }


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