package com.cg.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class TrainedInId implements Serializable {

    private int physician;   
    private int treatment;   
}