package com.cg.dto;

import jakarta.validation.constraints.*;

public class PatientDTO {

	@NotNull(message = "SSN cannot be null")
	@Min(value = 1, message = "SSN must be greater than 0")
	private Long ssn;

	@NotBlank(message = "Name cannot be empty")
	@Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters")
	private String name;

	@NotBlank(message = "Address cannot be empty")
	@Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
	private String address;

	@NotBlank(message = "Phone cannot be empty")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	private String phone;

	@NotNull(message = "Insurance ID cannot be null")
	@Min(value = 1, message = "Insurance ID must be greater than 0")
	private Integer insuranceId;

	@Min(value = 1, message = "Physician ID must be greater than 0")
	private Integer physicianId;

	public PatientDTO() {
	}

	public PatientDTO(Long ssn, String name, String address, String phone, Integer insuranceId, Integer physicianId) {
		this.ssn = ssn;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.insuranceId = insuranceId;
		this.physicianId = physicianId;
	}

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

	public Integer getPhysicianId() {
		return physicianId;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}
}