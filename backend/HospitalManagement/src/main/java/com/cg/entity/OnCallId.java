package com.cg.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Embeddable
public class OnCallId implements Serializable {

    @Column(name = "nurse")
    private Integer nurse;

    @Column(name = "blockfloor")
    private Integer blockFloor;

    @Column(name = "blockcode")
    private Integer blockCode;

    public OnCallId() {
    }

    public OnCallId(Integer nurse, Integer blockFloor, Integer blockCode) {
        this.nurse = nurse;
        this.blockFloor = blockFloor;
        this.blockCode = blockCode;
    }


    public Integer getNurse() {
        return nurse;
    }

    public void setNurse(Integer nurse) {
        this.nurse = nurse;
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
}