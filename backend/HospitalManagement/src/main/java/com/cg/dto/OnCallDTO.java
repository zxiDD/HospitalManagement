package com.cg.dto;

import java.time.LocalDateTime;

public class OnCallDTO {

    private Integer nurseId;
    private Integer blockFloor;
    private Integer blockCode;

    private LocalDateTime onCallStart;
    private LocalDateTime onCallEnd;

    public OnCallDTO() {
    }

	public OnCallDTO(Integer nurseId, Integer blockFloor, Integer blockCode, LocalDateTime onCallStart,
			LocalDateTime onCallEnd) {
		super();
		this.nurseId = nurseId;
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