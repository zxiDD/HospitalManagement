package com.cg.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class TrainedInDTO {
	
    @NotNull(message = "Physician ID is required")
    @Min(value = 1, message = "Physician ID must be positive")
    private Integer physicianId;

    @NotNull(message = "Treatment ID is required")
    @Min(value = 1, message = "Treatment ID must be positive")
    private Integer treatmentId;

    @NotNull(message = "Certification date is required")
    @PastOrPresent(message = "Certification date cannot be future")
    private LocalDate certificationDate;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be future")
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