package com.cg.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class PrescribesId implements Serializable {

    private Integer physician;
    private Long patient;
    private Integer medication;
    private LocalDateTime date;

    public PrescribesId() {}

    public PrescribesId(Integer physician, Long patient, Integer medication, LocalDateTime date) {
        this.physician = physician;
        this.patient = patient;
        this.medication = medication;
        this.date = date;
    }

    // Getters & Setters
    public Integer getPhysician() { return physician; }
    public void setPhysician(Integer physician) { this.physician = physician; }

    public Long getPatient() { return patient; }
    public void setPatient(Long patient) { this.patient = patient; }

    public Integer getMedication() { return medication; }
    public void setMedication(Integer medication) { this.medication = medication; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    // equals & hashCode (VERY IMPORTANT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrescribesId)) return false;
        PrescribesId that = (PrescribesId) o;
        return Objects.equals(physician, that.physician) &&
               Objects.equals(patient, that.patient) &&
               Objects.equals(medication, that.medication) &&
               Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physician, patient, medication, date);
    }
}