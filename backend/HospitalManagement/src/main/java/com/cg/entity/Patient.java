package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // Relationship
    @ManyToOne
    @JoinColumn(name = "pcp", nullable = false)
    private Physician physician;

    // Constructors
    public Patient() {}

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
}