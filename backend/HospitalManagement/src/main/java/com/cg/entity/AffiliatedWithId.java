package com.cg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AffiliatedWithId implements Serializable {

    @Column(name = "physician")
    private Integer physicianId;

    @Column(name = "department")
    private Integer departmentId;

}