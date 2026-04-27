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

	public Integer getPhysicianId() {
		return physicianId;
	}

	public AffiliatedWithId() {
		super();
	}

	public AffiliatedWithId(Integer physicianId, Integer departmentId) {
		super();
		this.physicianId = physicianId;
		this.departmentId = departmentId;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

}