package com.cg.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Embeddable
public class TrainedInId implements Serializable {

    @Column(name = "physician")
    private Integer physician;

    @Column(name = "treatment")
    private Integer treatment;

    public TrainedInId() {
    }

    public TrainedInId(Integer physician, Integer treatment) {
        this.physician = physician;
        this.treatment = treatment;
    }


    public Integer getPhysician() {
        return physician;
    }

    public void setPhysician(Integer physician) {
        this.physician = physician;
    }

    public Integer getTreatment() {
        return treatment;
    }

    public void setTreatment(Integer treatment) {
        this.treatment = treatment;
    }
}