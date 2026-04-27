package com.cg.dto;

public class PhysicianDTO {

    private Integer employeeId;
    private String name;
    private String position;

    public PhysicianDTO() {}

    public PhysicianDTO(Integer employeeId, String name, String position) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
    }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}