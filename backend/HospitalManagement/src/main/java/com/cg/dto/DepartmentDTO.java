package com.cg.dto;

import jakarta.validation.constraints.*;

public class DepartmentDTO {

    private Integer departmentId;

    @NotBlank(message = "Department name is required")
    @Size(max = 30, message = "Department name must not exceed 30 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Only letters and spaces are allowed")
    private String name;

    @NotNull(message = "Head (Physician ID) is required")
    private Integer headId;

    private String headName;

    public DepartmentDTO() {}

    public DepartmentDTO(Integer departmentId, String name, Integer headId, String headName) {
        this.departmentId = departmentId;
        this.name = name;
        this.headId = headId;
        this.headName = headName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }
}