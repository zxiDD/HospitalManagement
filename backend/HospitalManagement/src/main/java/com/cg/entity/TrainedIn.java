package com.cg.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "TrainedIn")
public class TrainedIn {

    @EmbeddedId
    private TrainedInId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician", referencedColumnName = "EmployeeID")
    private Physician physician;

    @ManyToOne
    @MapsId("treatment")
    @JoinColumn(name = "treatment", referencedColumnName = "Code")
    private Procedures treatment;

    @Column(name = "CertificationDate")
    private LocalDate certificationDate;

    @Column(name = "CertificationExpires")
    private LocalDate certificationExpires;

    public TrainedIn() {
    }

    public TrainedIn(TrainedInId id, Physician physician, Procedures treatment,
                     LocalDate certificationDate, LocalDate certificationExpires) {
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

    public LocalDate getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(LocalDate certificationDate) {
        this.certificationDate = certificationDate;
    }

    public LocalDate getCertificationExpires() {
        return certificationExpires;
    }

    public void setCertificationExpires(LocalDate certificationExpires) {
        this.certificationExpires = certificationExpires;
    }
}