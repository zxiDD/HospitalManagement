package com.cg.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class OnCallId implements Serializable {

    private int nurse;        // FK → Nurse
    private int blockFloor;   // FK → Block
    private int blockCode;    // FK → Block
}