package com.cg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Digits;

public class PhysicianDTO {

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be greater than 0")
    private Integer employeeId;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets and spaces")
    private String name;

    @NotBlank(message = "Position cannot be empty")
    @Size(min = 2, max = 50, message = "Position must be between 2 and 50 characters")
    private String position;

    @NotNull(message = "SSN cannot be null")
    @Digits(integer = 12, fraction = 0, message = "SSN must be a valid numeric value")
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

    public Long getSsn() { return ssn; }
    public void setSsn(Long ssn) { this.ssn = ssn; }
}