package com.cg.dto;

import java.time.LocalDateTime;

public class OnCallDTO {

    private int nurseId;
    private int blockFloor;
    private int blockCode;

    private LocalDateTime onCallStart;
    private LocalDateTime onCallEnd;

    public OnCallDTO() {
    }

    public OnCallDTO(int nurseId, int blockFloor, int blockCode,
                     LocalDateTime onCallStart, LocalDateTime onCallEnd) {
        this.nurseId = nurseId;
        this.blockFloor = blockFloor;
        this.blockCode = blockCode;
        this.onCallStart = onCallStart;
        this.onCallEnd = onCallEnd;
    }


    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }

    public int getBlockFloor() {
        return blockFloor;
    }

    public void setBlockFloor(int blockFloor) {
        this.blockFloor = blockFloor;
    }

    public int getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(int blockCode) {
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