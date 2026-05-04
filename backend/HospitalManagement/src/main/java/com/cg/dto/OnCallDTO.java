package com.cg.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OnCallDTO {


    @NotNull(message = "Nurse ID is required")
    @Min(value = 1, message = "Nurse ID must be positive")
    private Integer nurseId;
    
    private String nurseName;

    @NotNull(message = "Block Floor is required")
    @Min(value = 1, message = "Block Floor must be positive")
    private Integer blockFloor;

    @NotNull(message = "Block Code is required")
    @Min(value = 1, message = "Block Code must be positive")
    private Integer blockCode;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be present or future")
    private LocalDateTime onCallStart;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be future")
    private LocalDateTime onCallEnd;

    public OnCallDTO() {
    }

	public OnCallDTO(Integer nurseId, String nurseName, Integer blockFloor, Integer blockCode, LocalDateTime onCallStart,
			LocalDateTime onCallEnd) {
		super();
		this.nurseId = nurseId;
		this.nurseName = nurseName;
		this.blockFloor = blockFloor;
		this.blockCode = blockCode;
		this.onCallStart = onCallStart;
		this.onCallEnd = onCallEnd;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public Integer getBlockFloor() {
		return blockFloor;
	}

	public void setBlockFloor(Integer blockFloor) {
		this.blockFloor = blockFloor;
	}

	public Integer getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(Integer blockCode) {
		this.blockCode = blockCode;
	}

	public LocalDateTime getOnCallStart() {
		return onCallStart;
	}

	public void setOnCallStart(LocalDateTime onCallStart) {
		this.onCallStart = onCallStart;
	}

	public LocalDateTime getOnCallEnd() {
		return onCallEnd;
	}

	public void setOnCallEnd(LocalDateTime onCallEnd) {
		this.onCallEnd = onCallEnd;
	}


}