package com.cg.dto;

public class PatientDTO {

    private Long ssn;
    private String name;
    private String address;
    private String phone;
    private Integer insuranceId;
    private Integer physicianId;

    public PatientDTO() {}

    public PatientDTO(Long ssn, String name, String address,
                      String phone, Integer insuranceId, Integer physicianId) {
        this.ssn = ssn;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.insuranceId = insuranceId;
        this.physicianId = physicianId;
    }

    public Long getSsn() { return ssn; }
    public void setSsn(Long ssn) { this.ssn = ssn; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getInsuranceId() { return insuranceId; }
    public void setInsuranceId(Integer insuranceId) { this.insuranceId = insuranceId; }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }
}