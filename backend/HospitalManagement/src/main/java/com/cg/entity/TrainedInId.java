package com.cg.entity;
@Embeddable
public class TrainedInId implements Serializable {

    private int physician;   // FK → Physician
    private int treatment;   // FK → Procedures
}