package com.cg.dto;

public class AffiliatedWithDTO {

    private Integer physicianId;
    private String physicianName;      // optional

    private Integer departmentId;
    private String departmentName;     // optional

    private Boolean primaryAffiliation;

    // Constructors
    public AffiliatedWithDTO() {}

    public AffiliatedWithDTO(Integer physicianId, String physicianName,
                              Integer departmentId, String departmentName,
                              Boolean primaryAffiliation) {
        this.physicianId = physicianId;
        this.physicianName = physicianName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.primaryAffiliation = primaryAffiliation;
    }

    // Getters & Setters
    public Integer getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(Integer physicianId) {
        this.physicianId = physicianId;
    }

    public String getPhysicianName() {
        return physicianName;
    }

    public void setPhysicianName(String physicianName) {
        this.physicianName = physicianName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Boolean getPrimaryAffiliation() {
        return primaryAffiliation;
    }

    public void setPrimaryAffiliation(Boolean primaryAffiliation) {
        this.primaryAffiliation = primaryAffiliation;
    }
}