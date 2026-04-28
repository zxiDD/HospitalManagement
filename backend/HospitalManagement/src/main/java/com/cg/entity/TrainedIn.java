package com.cg.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "trained_in")
public class TrainedIn {

    @EmbeddedId
    private TrainedInId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician;

    @ManyToOne
    @MapsId("treatment")
    @JoinColumn(name = "treatment", referencedColumnName = "code")
    private Procedures treatment;

    @Column(name = "certificationdate")
    private LocalDateTime certificationDate;

    @Column(name = "certificationexpires")
    private LocalDateTime certificationExpires;

    public TrainedIn() {
    }

    public TrainedIn(TrainedInId id, Physician physician, Procedures treatment,
                     LocalDateTime certificationDate, LocalDateTime certificationExpires) {
        this.id = id;
        this.physician = physician;
        this.treatment = treatment;
        this.certificationDate = certificationDate;
        this.certificationExpires = certificationExpires;
    }


    public TrainedInId getId() {
        return id;
    }

    public void setId(TrainedInId id) {
        this.id = id;
    }

    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    public Procedures getTreatment() {
        return treatment;
    }

    public void setTreatment(Procedures treatment) {
        this.treatment = treatment;
    }

    public LocalDateTime getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(LocalDateTime certificationDate) {
        this.certificationDate = certificationDate;
    }

    public LocalDateTime getCertificationExpires() {
        return certificationExpires;
    }

    public void setCertificationExpires(LocalDateTime certificationExpires) {
        this.certificationExpires = certificationExpires;
    }
}