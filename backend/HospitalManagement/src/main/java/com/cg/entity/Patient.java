package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Patient")
public class Patient {

	@Id
	@Column(name = "ssn")
	private Long ssn;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "insuranceid", nullable = false)
	private Integer insuranceId;

	@Column(name = "is_active")
	private Boolean isActive = true;

	// Relationship
	@ManyToOne
	@JoinColumn(name = "pcp")
	private Physician physician;

	// Constructors
	public Patient() {
	}

	public Patient(Long ssn, String name, String address, String phone, Integer insuranceId, Physician physician) {
		this.ssn = ssn;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.insuranceId = insuranceId;
		this.physician = physician;
	}

	// Getters & Setters
	public Long getSsn() {
		return ssn;
	}

	public void setSsn(Long ssn) {
		this.ssn = ssn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Physician getPhysician() {
		return physician;
	}

	public void setPhysician(Physician physician) {
		this.physician = physician;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}