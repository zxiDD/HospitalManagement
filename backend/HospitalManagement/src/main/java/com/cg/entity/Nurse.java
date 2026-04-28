package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Nurse")
public class Nurse {

    @Id
    @Column(name = "employeeid")
    private Integer employeeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "registered", nullable = false)
    private Boolean registered;

    @Column(name = "ssn", nullable = false)
    private Long ssn;

    // Constructors
    public Nurse() {}

    public Nurse(Integer employeeId, String name, String position, Boolean registered, Long ssn) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.registered = registered;
        this.ssn = ssn;
    }

    // Getters & Setters
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public Long getSsn() {
        return ssn;
    }

    public void setSsn(Long ssn) {
        this.ssn = ssn;
    }
}