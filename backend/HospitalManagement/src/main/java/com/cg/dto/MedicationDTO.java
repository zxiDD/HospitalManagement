package com.cg.dto;

public class MedicationDTO {

    private Integer code;
    private String name;
    private String brand;
    private String description;

    // Constructors
    public MedicationDTO() {}

    public MedicationDTO(Integer code, String name, String brand, String description) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.description = description;
    }

    // Getters & Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}