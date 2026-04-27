package com.cg.dto;

import java.time.LocalDate;

public class TrainedInDTO {

    private Integer physicianId;
    private Integer treatmentId;

    private LocalDate certificationDate;
    private LocalDate certificationExpires;

    public TrainedInDTO() {
    }
    

	public TrainedInDTO(Integer physicianId, Integer treatmentId, LocalDate certificationDate,
			LocalDate certificationExpires) {
		super();
		this.physicianId = physicianId;
		this.treatmentId = treatmentId;
		this.certificationDate = certificationDate;
		this.certificationExpires = certificationExpires;
	}


	public Integer getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}

	public Integer getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(Integer treatmentId) {
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