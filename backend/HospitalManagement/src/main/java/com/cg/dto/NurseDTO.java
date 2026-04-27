package com.cg.dto;

public class NurseDTO {

    private Integer employeeId;
    private String name;
    private String position;
    private Boolean registered;

    public NurseDTO() {}

    public NurseDTO(Integer employeeId, String name, String position, Boolean registered) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.registered = registered;
    }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Boolean getRegistered() { return registered; }
    public void setRegistered(Boolean registered) { this.registered = registered; }
}