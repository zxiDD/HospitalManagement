package com.cg.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Prescribes")
public class Prescribes {

    @EmbeddedId
    private PrescribesId id;

    // Relationships
    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician")
    private Physician physician;

    @ManyToOne
    @MapsId("patient")
    @JoinColumn(name = "patient")
    private Patient patient;

    @ManyToOne
    @MapsId("medication")
    @JoinColumn(name = "medication")
    private Medication medication;

    @Column(name = "dose", nullable = false)
    private String dose;

    @ManyToOne
    @JoinColumn(name = "appointment")
    private Appointment appointment;

    // Constructors
    public Prescribes() {}

    public Prescribes(PrescribesId id, Physician physician, Patient patient,
                      Medication medication, String dose, Appointment appointment) {
        this.id = id;
        this.physician = physician;
        this.patient = patient;
        this.medication = medication;
        this.dose = dose;
        this.appointment = appointment;
    }

    // Getters & Setters
    public PrescribesId getId() { return id; }
    public void setId(PrescribesId id) { this.id = id; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { this.medication = medication; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
}