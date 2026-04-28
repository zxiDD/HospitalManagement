package com.cg.dto;

public class PhysicianDTO {

    private Integer employeeId;
    private String name;
    private String position;
    private Long ssn;

    public PhysicianDTO() {}

    public PhysicianDTO(Integer employeeId, String name, String position, Long ssn) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.ssn = ssn;
    }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Long getSsn() {
		return ssn;
	}

	public void setSsn(Long ssn) {
		this.ssn = ssn;
	}
}