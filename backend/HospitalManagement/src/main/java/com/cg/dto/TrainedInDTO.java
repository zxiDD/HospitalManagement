package com.cg.dto;

import java.time.LocalDate;

public class TrainedInDTO {

    private int physicianId;
    private int treatmentId;

    private LocalDate certificationDate;
    private LocalDate certificationExpires;

    public TrainedInDTO() {
    }

    public TrainedInDTO(int physicianId, int treatmentId,
                        LocalDate certificationDate, LocalDate certificationExpires) {
        this.physicianId = physicianId;
        this.treatmentId = treatmentId;
        this.certificationDate = certificationDate;
        this.certificationExpires = certificationExpires;
    }


    public int getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(int physicianId) {
        this.physicianId = physicianId;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
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